package patterns.structural

// 3rd party library code

interface IDisplay {
    fun diplay(content: String)
}

class Display : IDisplay {
    override fun diplay(content: String) {
        println("> $content")
    }
}

// our own code

interface IDisplayAdapter {
    fun push(num: Int)
}

class DisplayAdapter(private val display: IDisplay) : IDisplayAdapter {
    override fun push(num: Int) {
        val convertedValue = num.toString()
        display.diplay(convertedValue)
    }
}

fun main() {
    val display: IDisplay = Display()
    val adapter: IDisplayAdapter = DisplayAdapter(display)

    adapter.push(34)
}