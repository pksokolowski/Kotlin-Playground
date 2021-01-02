package patterns.creational

class EngineFactory() {
    fun engineForVehicle(vehicle: Vehicle) =
        when (vehicle) {
            is Bike -> HumanEngine()
            is Car -> CombustionEngine()
            is Cab -> HorseEngine()
        }
}

sealed class Vehicle
object Bike : Vehicle()
object Car : Vehicle()
object Cab : Vehicle()

open class Engine
class HumanEngine : Engine()
class CombustionEngine : Engine()
class HorseEngine : Engine()

fun main() {
    val factory = EngineFactory()
    val vehicles = listOf(Bike, Car, Cab)
    vehicles.forEach { vehicle ->
        val engineMatched = factory.engineForVehicle(vehicle)
        println("for ${vehicle::class.simpleName} a factory created the following engine: ${engineMatched::class.simpleName}")
    }
}

