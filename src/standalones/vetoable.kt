package standalones

import kotlin.properties.Delegates

fun main() {

    object {
        var kawka by Delegates.vetoable("hello", { _, oldValue, newValue ->
            newValue.length > oldValue.length
        })
    }.apply {
        kawka = "chel"
        println(kawka)
        kawka = "quite a few letters more!"
        println(kawka)
    }

}