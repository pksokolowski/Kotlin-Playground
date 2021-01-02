package patterns.creational

object SomeSingleton {
    private var resource: String = ""

    @Synchronized
    fun saveString(string: String) {
        resource = string
    }

    @Synchronized
    fun readString(): String {
        return resource
    }
}

fun main() {
    println("save something to singleton and read it back")
    SomeSingleton.saveString("123")
    println(SomeSingleton.readString())
}