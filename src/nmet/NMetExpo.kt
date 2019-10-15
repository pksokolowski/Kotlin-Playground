package nmet

import Expo

object NMetExpo : Expo{
    override fun runSample() {
        println(
            atLeastNMet(2,
                { println("first checked"); true },
                { println("second checked");false },
                { println("third checked"); true },
                { println("fourth checked"); false }
            )
        )
    }
}