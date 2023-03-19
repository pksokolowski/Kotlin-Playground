package standalones

import java.nio.ByteBuffer
import kotlin.experimental.xor
import kotlin.math.ceil
import kotlin.math.floor


private val constant = "aaaabbbbccccdddd".toByteArray(charset = Charsets.UTF_8)
private val CONSTANT_LEN = 16
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
    repeat(blocksNeeded) { blockNumber ->
        // update block
        intBlock.setBlockNumber(blockNumber)

        // internal state transformation
        val transformedBlock = getTransformedBlock(intBlock)

        // output streamKey chunk
        val chunk = intBlock xor transformedBlock
        val wantedLen =
            if (blockNumber + 1 < fullBlocksFitting || fullBlocksFitting == blocksNeeded) {
                chunk.size
            } else {
                streamKey.size - (fullBlocksFitting * BLOCK_SIZE)
            }
        chunk.toByteArray().copyInto(streamKey, blockNumber * chunk.size, 0, wantedLen)
    }

    // apply streamKey
    return this xor streamKey
}

private fun IntArray.setBlockNumber(blockNumber: Int) {
    this[12] = blockNumber
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
    val nonce = "aaaaaaaaaaaa".toByteArray()
    val key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".toByteArray()
    val plainText = "abc".toByteArray()

    val cipherText = plainText.applyNaiveIdempotentCipher(nonce, key)
    val deciphered = cipherText.applyNaiveIdempotentCipher(nonce, key)

    println(
        """Encrypted: ${plainText.joinToString()} and got:
        |${cipherText.joinToString()}
        |which in turn was deciphered into:
        |${deciphered.joinToString()}
        |which is ${if (deciphered.contentEquals(plainText)) "equal" else "not equal"} to the original value.
    """.trimMargin()
    )
}