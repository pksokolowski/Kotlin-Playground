package standalones

interface Engine{
    fun run()
}

class CombustionEngine(): Engine{
    override fun run() {
        println("Running using $this")
    }

    override fun toString() = "Combustion engine"
}

class LightPlane(engine: Engine) : Engine by engine

fun main() {
    LightPlane(CombustionEngine()).run()
}