package patterns.behavioral

import kotlin.math.ceil
import kotlin.math.floor

data class Calculator(val name: String, private val roundingPolicy: (Double) -> Int) {
    fun divide(a: Int, b: Int): Int {
        val result = a / b.toDouble()
        return roundingPolicy(result)
    }
}

val ceilingRoundingPolicy = { result: Double -> ceil(result).toInt() }
val floorRoundingPolicy = { result: Double -> floor(result).toInt() }

fun main() {
    val ceilingCalculator = Calculator("ceiling", ceilingRoundingPolicy)
    val floorCalculator = Calculator("floor", floorRoundingPolicy)

    val dividend = 5
    val divisor = 2

    listOf(ceilingCalculator, floorCalculator).forEach { calculator ->
        println(
            """
                
            With rounding policy: ${calculator.name}
            dividend = $dividend
            divisor = $divisor
            Rersult is: ${calculator.divide(dividend, divisor)}
        """.trimIndent()
        )
    }
}