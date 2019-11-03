import configurable.ConfigurableProcedureExpo
import nmet.NMetExpo
import using.DisposableExpo

fun main() {

    listOf(
        DisposableExpo,
        NMetExpo,
        ConfigurableProcedureExpo
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