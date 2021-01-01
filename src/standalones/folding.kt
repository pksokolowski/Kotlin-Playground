package standalones

fun main() {
    val list = listOf(1, 2, 3, 4)
        .fold(1) { partProduct, element ->
            element * partProduct
        }

    println(list)
}