package standalones

import java.lang.IllegalStateException


val idiomSamples = listOf<() -> String>(
    {
        val pocketContents = listOf("keys", "knife", "wallet", "pencil")
        if ("knife" in pocketContents) "probably a survivalist" else "potential threat"
    },
    {
        fun String.countXes() = count { it == 'x' }
        "xxx".countXes().toString()
    },
    {
        data class User(val name: String, val favoriteProduct: String)

        val users = listOf(
            User("Christian", "toy car"),
            User("Amanda", "dress"),
            User("Martha", "dress"),
            User("Camilla", "dress")
        )
        val favProducts = users.map { it.favoriteProduct }.toSet()
        "Favorite products: $favProducts"
    },
    {
        var a = 1
        var b = 2
        a = b.also { b = a }
        "a = $a, b = $b"
    },
    {
        data class Purchase(val value: Int, val isCardTransaction: Boolean)
        data class User(val name: String, val purchases: List<Purchase>)

        val users = listOf(
            User(
                "Amelia",
                listOf(
                    Purchase(650, false),
                    Purchase(300, false),
                    Purchase(400, true)
                )
            ),
            User(
                "Beth",
                listOf(
                    Purchase(300, true),
                    Purchase(400, true)
                )
            )
        )

        val mostlyCashUsers = users.filter { user ->
            val (cardTransactions, cashTransactions) = user.purchases.partition { it.isCardTransaction }
            cardTransactions.size < cashTransactions.size
        }.map { it.name }

        "Users who preferred cash: $mostlyCashUsers"
    },
    {
        data class Car(val wheelsCount: Int)

        val cars = listOf(
            Car(3),
            Car(4)
        )
        val myCar = cars.firstOrNull { it.wheelsCount == 4 }
        "I'd choose $myCar"
    },
    {
        val itRains = true
        val thingsToTake = "an umbrella".takeIf { itRains } ?: "sunglasses"
        "I'd take $thingsToTake with me"
    },
    {
        try {
            val isEarthFlat = false
            check(isEarthFlat)
            "let's drill our way to the other world!"
        } catch (e: IllegalStateException) {
            "nope, let's wait till it spins itself flat finally..."
        }
    },
    {
        val FLAG_ANGRY = 2
        val FLAG_RELAXED = 4
        val FLAG_CURIOUS = 8

        val flags = FLAG_CURIOUS or FLAG_ANGRY
        val flagsSetCount = flags.countOneBits()
        "Flags set = $flagsSetCount"
    },
    {
        val countries = listOf("Poland", "UK", "US")
        val weather = countries.map {
            when (it) {
                "Poland" -> "$it: getting warmer every year!"
                "UK" -> "$it: Willing to bet it rains right now."
                "US" -> "$it: Neven been to US :("
                else -> "$it: unknown too"
            }
        }
        "Weather in selected countries: $weather"
    }
)

fun main() {
    idiomSamples.forEach {
        println(it())
    }
}