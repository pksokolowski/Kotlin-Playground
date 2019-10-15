import nmet.NMetExpo
import using.DisposableExpo

fun main() {

    listOf(
        DisposableExpo,
        NMetExpo
    ).forEach {
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