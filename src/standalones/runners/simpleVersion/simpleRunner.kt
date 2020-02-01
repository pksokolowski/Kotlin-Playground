package standalones.runners.simpleVersion

fun <R> run(
    body: ((GuidMsisdnPair) -> R),
    onException: (e: Exception) -> Unit,
    onSuccess: (r: R) -> Unit
) {
    val pair = GuidMsisdnPair("kopkop", "123456789")
    try {
        val result = body.invoke(pair)
        onSuccess.invoke(result)
    } catch (e: Exception) {
        onException.invoke(e)
    }
}

data class GuidMsisdnPair(val guid: String?, val msisdn: String?)

open class GenericRequest(credentials: GuidMsisdnPair) {
    val guid = credentials.guid
    val msisdn = credentials.msisdn
}

class ConcreteRequest(creds: GuidMsisdnPair, pin: String) : GenericRequest(creds)

fun runRequest(request: ConcreteRequest) = "kopkopko"


fun main() {
    run(
        {
            runRequest(ConcreteRequest(it, "1234"))
        },
        onException = {

        },
        onSuccess = {

        }
    )
}