import configurable.ConfigurableProcedureExpo
import nmet.NMetExpo
import using.DisposableExpo
import whenIsReplacement.WhenIsReplacementExpo

fun main() {

    listOf(
        DisposableExpo,
        NMetExpo,
        ConfigurableProcedureExpo,
        WhenIsReplacementExpo
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