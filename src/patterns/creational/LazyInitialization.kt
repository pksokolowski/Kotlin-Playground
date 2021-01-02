package patterns.creational

fun main() {
    val lazyDependency by lazy { MemoryHeavyDependency() }

    println("main running...")
    println("now need the expensive dependency to get the following data: ${lazyDependency.hugeList}")
}

class MemoryHeavyDependency {
    val hugeList = listOf("1", "2", "3")

    init {
        println("Created ${this::class.simpleName}")
    }
}