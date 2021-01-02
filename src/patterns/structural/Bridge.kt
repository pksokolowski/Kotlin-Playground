package patterns.structural

interface Computer {
    fun turnOn()
}

interface Initializable {
    fun onPowerUp() {}
}

class BasicComputer(private val cpu: CPU, private val disc: Disc, private val cdDrive: CDDrive) : Computer {
    override fun turnOn() {
        println("cyk")
        cpu.onPowerUp()
        cdDrive.onPowerUp()
        disc.onPowerUp()
    }
}

interface CPU : Initializable

class Intel1 : CPU
class AMD1 : CPU

interface Disc : Initializable

class SSD : Disc
class HDD : Disc {
    override fun onPowerUp() {
        println("wziiiii")
    }
}

interface CDDrive : Initializable

class CDReader : CDDrive
class CDBurner : CDDrive

fun main() {
    val computer = BasicComputer(Intel1(), HDD(), CDReader())
    computer.turnOn()
}
