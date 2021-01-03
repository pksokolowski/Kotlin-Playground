package patterns.structural

import kotlin.random.Random

interface Disk {
    fun readAt(address: Int): Byte?
    fun writeAt(address: Int, bytes: ByteArray): OperationResult
}

enum class OperationResult {
    SUCCESS,
    FAILURE
}

class HddDisk : Disk {
    private val data = ByteArray(500) { Random.Default.nextBytes(1)[0] }

    override fun readAt(address: Int): Byte? {
        println("reading from physical disk...")
        Thread.sleep(1000)
        return data.getOrNull(address)
    }

    override fun writeAt(address: Int, bytes: ByteArray): OperationResult {
        println("writing to physical disk...")
        Thread.sleep(2000)
        if (address + bytes.size > data.lastIndex) return OperationResult.FAILURE

        for (i in bytes.indices) {
            data[address + i] = bytes[i]
        }

        return OperationResult.SUCCESS
    }
}

class DiskProxy(private val disk: Disk) : Disk {
    private val cache = hashMapOf<Int, Byte?>()

    override fun readAt(address: Int): Byte? {
        println("Reading at address $address, by proxy")
        val value = cache[address] ?: disk.readAt(address).also { cache[address] = it }
        println("Read value: $value")
        return value
    }

    override fun writeAt(address: Int, bytes: ByteArray): OperationResult {
        println("writing at address $address and ${bytes.size} onwards, by proxy")
        invalidateCache(address..(address + bytes.size))
        return disk.writeAt(address, bytes)
    }

    private fun invalidateCache(range: IntRange) {
        cache.keys
            .filter { it in range }
            .forEach { cache.remove(it) }
    }
}

fun main() {
    val disk = HddDisk()
    val proxy = DiskProxy(disk)

    println("--- first, lets save some data so we can query it later ---")
    proxy.writeAt(100, "abcd".toByteArray())

    println("\n--- now, lets read it for the first time ---")
    proxy.readAt(100)

    println("\n--- let's read it once more, it should come from cache this time ---")
    proxy.readAt(100)

    println("\n--- lets write some data now to invalidate the cache and try again ---")
    proxy.writeAt(99, "123".toByteArray())
    proxy.readAt(100)
}
