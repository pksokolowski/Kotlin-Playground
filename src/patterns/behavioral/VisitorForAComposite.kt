package patterns.behavioral

interface Visitor {
    fun visit(composite: Composite)
    fun visit(textualContent: TextualContent)
    fun visit(frontCover: FrontCover)
}

class DocumentVisitor() : Visitor {
    override fun visit(composite: Composite) {
        println("Visited a composite, looking forward to component visits.")
    }

    override fun visit(textualContent: TextualContent) {
        println("visiting textualContent, found: ${textualContent.text}")
    }

    override fun visit(frontCover: FrontCover) {
        println("visiting front cover")
    }
}

// Composite pattern applied below for demo of visitor

abstract class Component {
    abstract fun accept(v: Visitor)
}

open class Composite : Component() {
    private val components = mutableListOf<Component>()

    fun add(vararg components: Component) {
        this.components.addAll(components)
    }

    override fun accept(v: Visitor) {
        components.forEach { it.accept(v) }
    }
}

class Document : Composite()

class TextualContent(val text: String) : Component() {
    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

class FrontCover : Component() {
    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

fun main() {
    val document = Document().apply {
        add(TextualContent("Some interesting text content"))
        add(FrontCover())
    }

    val visitor = DocumentVisitor()

    document.accept(visitor)
}