package standalones.whenIsReplacement.whenIs

abstract class SendMessageResponse
class SuccessSendMessageResponse(val messageId: Long) : SendMessageResponse()
class ErrorSendMessageResponse(val errorCode: Int, val message: String) : SendMessageResponse()

class BusinessLogic {
    fun handleResponse(response: SendMessageResponse) {
        when (response) {
            is SuccessSendMessageResponse -> println("successfully sent message with id = ${response.messageId}")
            is ErrorSendMessageResponse -> println("Error no ${response.errorCode}. ${response.message}")
        }
    }
}

fun main() {
    val businessLogic = BusinessLogic()

    listOf(
        SuccessSendMessageResponse(1),
        ErrorSendMessageResponse(
            2,
            "Some non-localized error message :P"
        )
    ).forEach {
        businessLogic.handleResponse(it)
    }
}