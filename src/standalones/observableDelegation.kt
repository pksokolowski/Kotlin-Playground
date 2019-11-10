package standalones

import kotlin.properties.Delegates

class Thing() {
    var name by Delegates.observable("FancyPants") { prop, old, new ->
        println("a change has occured in ${prop.name}! was $old, is $new")
    }
}

fun main() {
    val a = Thing()
    a.name = "Zębatka"
}