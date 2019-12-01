package standalones

interface ClosedShape {
    fun area(): Int
}

class Rectangle(val width: Int, val height: Int) : ClosedShape {
    override fun area() = width * height
}

class Window(private val bounds: ClosedShape) : ClosedShape by bounds

fun main() {
    val rect = Rectangle(100, 100)
    val window = Window(rect)

    println(window.area())
}