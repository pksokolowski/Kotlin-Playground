package using

import Expo

object DisposableExpo : Expo {
    override fun runSample() {
        val obj = object : Disposable{
            override fun dispose() {
                println("disposing of this one")
            }
            var someVariable: Int = 3
        }

        obj useIn {
            println("""
                running some block of code on the disposable object
                someVariable in it has value: $someVariable
            """.trimIndent())
        }
    }
}