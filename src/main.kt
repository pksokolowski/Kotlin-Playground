import using.DisposableExpo

fun main() {

    listOf(DisposableExpo).forEach {
        println(
            """
            
            --------------------------------
            Running sample: ${it::class.simpleName}
            --------------------------------
        """.trimIndent()
        )
        it.runSample()
    }
}