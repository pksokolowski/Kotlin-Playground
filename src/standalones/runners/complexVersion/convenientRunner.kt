package standalones.runners.complexVersion

fun <R> run(
    body: (Convenience.() -> R),
    onException: (e: Exception) -> Unit,
    onSuccess: (r: R) -> Unit
) {
    val pair = Convenience("kopkop", "123456789")
    try {
        val result = body.invoke(pair)
        onSuccess.invoke(result)
    } catch (e: Exception) {
        onException.invoke(e)
    }
}

data class Convenience(val guid: String?, val msisdn: String?)

open class GenericRequest(val guid: String?, val msisdn: String?)

class ConcreteRequest(guid: String?, msisdn: String?, pin: String) : GenericRequest(guid, msisdn)

fun runRequest(request: ConcreteRequest) = "kopkopko"


fun main() {
    run(
        {
            runRequest(ConcreteRequest(guid, msisdn, "1234"))
        },
        onException = {

        },
        onSuccess = {

        }
    )
}