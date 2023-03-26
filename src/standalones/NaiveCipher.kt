@file:OptIn(ExperimentalUnsignedTypes::class)

package standalones

import java.nio.ByteBuffer
import kotlin.experimental.xor
import kotlin.math.ceil
import kotlin.math.floor


private val constant = "expand 32-byte k".toByteArray(charset = Charsets.UTF_8)
private const val CONSTANT_LEN = 16
private const val BLOCK_NUMBER_LEN = 4
private const val NONCE_LEN = 12
private const val KEY_LEN = 32
private const val BLOCK_SIZE = 64
private const val ROUNDS_NUMBER = 20
private const val SUB_ROUNDS_NUMBER = 4

/**
 * The cipher is a naive, educational-purposes-only implementation and design.
 * Do not use in production.
 */
private fun ByteArray.applyNaiveIdempotentCipher(nonce: ByteArray, key: ByteArray): ByteArray {
    require(this.isNotEmpty()) { "ciphertext/plaintext input was empty" }
    require(key.size == KEY_LEN) { "key size is ${key.size} bytes while len of $KEY_LEN is required" }
    require(nonce.size == NONCE_LEN) { "nonce size is ${nonce.size} bytes while len of $NONCE_LEN is required" }

    // create initial internal state
    val block = ByteArray(BLOCK_SIZE)
    constant.copyInto(block, 0, 0)
    key.copyInto(block, CONSTANT_LEN, 0)
    // block number is skipped here, will be set later
    nonce.copyInto(block, CONSTANT_LEN + KEY_LEN + BLOCK_NUMBER_LEN, 0)

    // convert the block to a 32bit integers array, for convenience in later calculations
    val intBlock = IntArray(16) { block.extractIntAt(it * 4) }

    // generate stream key
    val streamKey = ByteArray(this.size)
    val blocksNeeded = ceil(this.size / BLOCK_SIZE.toDouble()).toInt()
    val fullBlocksFitting = floor(this.size / BLOCK_SIZE.toDouble()).toInt()
    repeat(blocksNeeded) { blockIndex ->
        // update block // block counter will start with 1, so one is added
        intBlock.setBlockNumber(blockIndex + 1)

        // internal state transformation
        val transformedBlock = getTransformedBlock(intBlock)

        // output streamKey chunk
        val chunk = intBlock.mapIndexed { i, originWord -> originWord + transformedBlock[i] }.toIntArray()
        val byteChunk = chunk.toByteArray()
        val wantedLen =
            if (blockIndex + 1 < fullBlocksFitting || fullBlocksFitting == blocksNeeded) {
                byteChunk.size
            } else {
                streamKey.size - (fullBlocksFitting * BLOCK_SIZE)
            }

        byteChunk.copyInto(streamKey, blockIndex * byteChunk.size, 0, wantedLen)
    }

    // apply streamKey
    return this xor streamKey
}

private fun IntArray.setBlockNumber(blockNumber: Int) {
    this[12] = blockNumber
}

private fun getTransformedBlock(block: IntArray): IntArray {
    val transformedBlock = IntArray(block.size) { block[it] }

    repeat(ROUNDS_NUMBER) { roundNumber ->
        val roundType = if (roundNumber.mod(2) == 0) RoundType.HORIZONTAL else RoundType.DIAGONAL
        runRound(transformedBlock, roundType)
    }

    return transformedBlock
}

private fun runRound(block: IntArray, roundType: RoundType) {
    repeat(SUB_ROUNDS_NUMBER) { subRoundNumber ->
        val i = when (roundType) {
            RoundType.HORIZONTAL -> Indexes(
                a = 0 + subRoundNumber,
                b = 4 + subRoundNumber,
                c = 8 + subRoundNumber,
                d = 12 + subRoundNumber,
            )
            RoundType.DIAGONAL -> Indexes(
                a = 0 + subRoundNumber,
                b = 4 + (1 + subRoundNumber).mod(4),
                c = 8 + (2 + subRoundNumber).mod(4),
                d = 12 + (3 + subRoundNumber).mod(4),
            )
        }

        block[i.a] = block[i.a] + block[i.b]
        block[i.d] = block[i.d].xor(block[i.a])
        block[i.d] = block[i.d].rotateLeft(16)
        block[i.c] = block[i.c] + block[i.d]
        block[i.b] = block[i.b].xor(block[i.c])
        block[i.b] = block[i.b].rotateLeft(12)
        block[i.a] = block[i.a] + block[i.b]
        block[i.d] = block[i.d].xor(block[i.a])
        block[i.d] = block[i.d].rotateLeft(8)
        block[i.c] = block[i.c] + block[i.d]
        block[i.b] = block[i.b].xor(block[i.c])
        block[i.b] = block[i.b].rotateLeft(7)
    }
}

private infix fun ByteArray.xor(other: ByteArray): ByteArray {
    require(this.size == other.size) { "cannot xor byteArrays of diff lengths" }
    return ByteArray(this.size).also { output ->
        repeat(this.size) { i ->
            output[i] = this[i] xor other[i]
        }
    }
}

private infix fun IntArray.xor(other: IntArray): IntArray {
    require(this.size == other.size) { "cannot xor byteArrays of diff lengths" }
    return IntArray(this.size).also { output ->
        repeat(this.size) { i ->
            output[i] = this[i] xor other[i]
        }
    }
}

private fun ByteArray.extractIntAt(startIndex: Int): Int {
    val bytes = this.sliceArray(startIndex..startIndex + 3)
        .apply { reverse() }
    return ByteBuffer.wrap(bytes).int
}

private fun IntArray.toByteArray(): ByteArray {
    val byteBuffer = ByteBuffer.allocate(this.size * 4)
    val intBuffer = byteBuffer.asIntBuffer()
    intBuffer.put(this)

    return byteBuffer.array()
}

private enum class RoundType {
    HORIZONTAL,
    DIAGONAL,
}

private class Indexes(
    val a: Int,
    val b: Int,
    val c: Int,
    val d: Int,
)

fun main() {
    checkAgainstTestVector()
    val nonce = "aaaaaaaaaaaa".toByteArray()
    val key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".toByteArray()
    val plainText = "abc".toByteArray()

    val cipherText = plainText.applyNaiveIdempotentCipher(nonce, key)
    val deciphered = cipherText.applyNaiveIdempotentCipher(nonce, key)

    println(
        """Encrypted: ${plainText.joinToString()} and got:
        |${cipherText.toUByteArray().joinToString()}
        |which in turn was deciphered into:
        |${deciphered.toUByteArray().joinToString()}
        |which is ${if (deciphered.contentEquals(plainText)) "equal" else "not equal"} to the original value.
    """.trimMargin()
    )
}

@OptIn(ExperimentalUnsignedTypes::class)
fun checkAgainstTestVector() {
    println("------")
    val nonce = ByteArray(12) { 0x00 }
    val key = ByteArray(32) { 0x00 }
    // all zeros because they are a neutral element of XOR, so the ciphertext will be just a stream key
    val plainText = ByteArray(64) { 0x00 }

    val cipherText = plainText.applyNaiveIdempotentCipher(nonce, key).toUByteArray()

    val expectedStreamKey = ubyteArrayOf(
        0x9fu, 0x07u, 0xe7u, 0xbeu, 0x55u, 0x51u, 0x38u, 0x7au, 0x98u, 0xbau, 0x97u,
        0x7cu, 0x73u, 0x2du, 0x08u, 0x0du, 0xcbu, 0x0fu, 0x29u, 0xa0u, 0x48u, 0xe3u, 0x65u, 0x69u, 0x12u, 0xc6u, 0x53u,
        0x3eu, 0x32u, 0xeeu, 0x7au, 0xedu, 0x29u, 0xb7u, 0x21u, 0x76u, 0x9cu, 0xe6u, 0x4eu, 0x43u, 0xd5u, 0x71u, 0x33u,
        0xb0u, 0x74u, 0xd8u, 0x39u, 0xd5u, 0x31u, 0xedu, 0x1fu, 0x28u, 0x51u, 0x0au, 0xfbu, 0x45u, 0xacu, 0xe1u, 0x0au,
        0x1fu, 0x4bu, 0x79u, 0x4du, 0x6fu
    )

    println(
        """
        Expected:
        ${expectedStreamKey.joinToString()}
        got:
        ${cipherText.joinToString()}
        ${if (expectedStreamKey.contentEquals(cipherText)) "correct" else "incorrect against test vector"} 
    """.trimIndent()
    )
}

fun kopkop() {
    val input =
        """
            9f 07 e7 be 55 51 38 7a 98 ba 97 7c 73 2d 08 0d
            cb 0f 29 a0 48 e3 65 69 12 c6 53 3e 32 ee 7a ed
            29 b7 21 76 9c e6 4e 43 d5 71 33 b0 74 d8 39 d5
            31 ed 1f 28 51 0a fb 45 ac e1 0a 1f 4b 79 4d 6f
        """.trimIndent()

    val output = input.replace("\n", " ").split(" ").map { "0x${it}u" }.joinToString()
    println(output)
}