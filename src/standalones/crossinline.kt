package standalones

private inline fun execute(crossinline block: () -> Unit) = block()

fun main() {
    execute {
        // local return is fine
        return@execute

        // non-local returns are prohibited by the crossinline keyword
        // so it's just like if the function weren't inlined in the first place.
//        return
    }
}