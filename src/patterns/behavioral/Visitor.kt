package patterns.behavioral

// simple visitor sample

interface ReportVisitor<out R> {
    fun visit(contract: SimpleTextBlockElement): R
    fun visit(contract: ParagraphsElement): R
}

interface ReportElement {
    fun <R> accept(v: ReportVisitor<R>): R
}

class SimpleTextBlockElement(val someField: String) : ReportElement {
    override fun <R> accept(v: ReportVisitor<R>): R = v.visit(this)
}

class ParagraphsElement(val paragraphs: List<String>) : ReportElement {
    override fun <R> accept(v: ReportVisitor<R>): R = v.visit(this)
}

class CharacterCountVisitor : ReportVisitor<Int> {
    override fun visit(contract: SimpleTextBlockElement): Int = contract.someField.length
    override fun visit(contract: ParagraphsElement): Int = contract.paragraphs.sumBy { it.length }
}


fun main() {
    val reportElements = listOf(
        SimpleTextBlockElement("Some Header"),
        ParagraphsElement(listOf("Lorem ipsum dolor sit amet", "123abc")),
    )

    val characterCountVisitor = CharacterCountVisitor()

    val characterCount = reportElements.sumBy { it.accept(characterCountVisitor) }
    println("Total character count in the report is: $characterCount")
}