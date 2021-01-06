package patterns.behavioral

data class Memento(val state: String)

class Originator(var state: String) {
    fun createMemento() = Memento(state)
    fun restoreMemento(memento: Memento) {
        state = memento.state
    }
}

// Note: Care taker doesn't know about the implementation behind creation of the Memento.
class CareTaker {
    private val mementoList = mutableListOf<Memento>()
    private var currentIndex = -1

    @Synchronized
    fun saveMemento(memento: Memento) {
        mementoList.add(memento)
    }

    private fun restore(index: Int): Memento? {
        currentIndex = index
        return mementoList.getOrNull(index)
    }

    @Synchronized
    fun undo(): Memento? {
        if (currentIndex == mementoList.lastIndex) return null
        currentIndex++
        return restore(currentIndex)
    }

    @Synchronized
    fun redo(): Memento? {
        if (currentIndex < 1) return null
        currentIndex--
        return restore(currentIndex)
    }
}

fun main() {
    val originator = Originator("Initial document state")
    val careTaker = CareTaker()

    println("We're starting with some initial document state = ${originator.state}")
    careTaker.saveMemento(originator.createMemento())

    originator.state = "Initial document state with some additions."
    careTaker.saveMemento(originator.createMemento())
    println("We've changed the state, now state = ${originator.state}")

    println("now we will call undo()")
    val restoredMemento = careTaker.undo()
    if (restoredMemento == null) {
        println("no previous state is known")
    } else {
        originator.restoreMemento(restoredMemento)
    }
    println("Current state of the dorument = ${originator.state}")
}