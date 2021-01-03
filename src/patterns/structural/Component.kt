package patterns.structural

open class Component(open val price: Int)

open class Composite : Component(0) {
    private val components = mutableListOf<Component>()

    override val price: Int
        get() = components.sumBy { it.price }

    fun add(vararg components: Component) {
        this.components.addAll(components)
    }
}

class Plane : Composite()
class Wings : Component(1_800_000)
class Wheels : Component(50_000)
class Body : Composite()
class Floor : Component(50_000)
class Seat : Component(400)
class Tube : Component(100_000)


fun main() {
    val planeBody = Body().apply {
        add(Floor())
        repeat(200) { add(Seat()) }
        add(Tube())
    }
    val plane = Plane().apply {
        add(Wings(), Wheels(), planeBody)
    }

    println("Built a plane, total cost of components = ${String.format("$%,d", plane.price)}")
}