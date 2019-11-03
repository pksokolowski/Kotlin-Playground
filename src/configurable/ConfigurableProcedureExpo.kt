package configurable

import Expo
import kotlin.random.Random

object ConfigurableProcedureExpo : Expo {
    override fun runSample() {
        val procedure = prepareSomeProcedure()

        procedure.add(TAG_BACKUP) { println("also doing backup...") }

        if (Random.nextBoolean()) procedure.remove(TAG_BACKUP)

        procedure()
    }

    private fun prepareSomeProcedure() = ConfigurableProcedure(
        { println("task 1 running") },
        { println("task 2 running") }
    ).apply {
        if (Random.nextBoolean()) {
            add { println("a third one, added later") }
        }
        add(::someImportantFunction)
    }

    private fun someImportantFunction() {
        println("doing something very important")
    }

    const val TAG_BACKUP = "backup"
}