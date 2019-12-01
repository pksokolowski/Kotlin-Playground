package standalones

data class User(val name: String, val email: String)

fun parseUser(input: String): User {
    val parts = input.split(",")
    return User(parts[0], parts[1].trim())
}

fun main() {
    val (name, email) = parseUser("Tom, example@example.com")

    println(
        """
        |hey user $name
        |your mail happens to be $email
    """.trimMargin()
    )
}