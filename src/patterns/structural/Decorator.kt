package patterns.structural

interface CarWash {
    fun wash()
}

class BasicCarWash() : CarWash {
    override fun wash() {
        println("washing programme started...")
    }
}

class MusicalCarWash(carWash: CarWash) : CarWash by carWash {
    fun playMusic() {
        println("play some music")
    }
}

fun main() {
    val carWash = BasicCarWash()
    val musicalCarWash = MusicalCarWash(carWash)

    musicalCarWash.wash()
    musicalCarWash.playMusic()
}