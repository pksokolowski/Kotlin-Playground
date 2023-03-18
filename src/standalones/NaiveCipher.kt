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
    block.setBlockNumber(1)
    nonce.copyInto(block, CONSTANT_LEN + KEY_LEN + BLOCK_NUMBER_LEN, 0)

    // generate stream key
    val streamKey = ByteArray(this.size)
    val blocksNeeded = ceil(this.size / BLOCK_SIZE.toDouble()).toInt()
    val fullBlocksFitting = floor(this.size / BLOCK_SIZE.toDouble()).toInt()
    repeat(blocksNeeded) { blockNumber ->
        // update block
        block.setBlockNumber(blockNumber)

        // internal state transformation
        val transformedBlock = ByteArray(block.size)
        block.copyInto(transformedBlock)
        repeat(BLOCK_SIZE) { transformedBlock[it] = transformedBlock[it].rotateLeft(3) }

        // output streamKey chunk
        val chunk = block xor transformedBlock
        val wantedLen =
            if (blockNumber + 1 < fullBlocksFitting || fullBlocksFitting == blocksNeeded) chunk.size else streamKey.size - (fullBlocksFitting * BLOCK_SIZE)
        chunk.copyInto(streamKey, blockNumber * chunk.size, 0, wantedLen)
    }

    // apply streamKey
    return this xor streamKey
}

private fun ByteArray.setBlockNumber(blockNumber: Int) {
    val initialIndex = CONSTANT_LEN + KEY_LEN
    val buffer = ByteBuffer.allocate(BLOCK_NUMBER_LEN).also {
        it.putInt(blockNumber)
    }
    val bytesOfNumber = ByteArray(BLOCK_NUMBER_LEN) { buffer[it] }
    bytesOfNumber.copyInto(this, initialIndex, BLOCK_NUMBER_LEN)
}

private infix fun ByteArray.xor(other: ByteArray): ByteArray {
    require(this.size == other.size) { "cannot xor byteArrays of diff lengths" }
    return ByteArray(this.size).also { output ->
        repeat(this.size) { i ->
            output[i] = this[i] xor other[i]
        }
    }
}

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