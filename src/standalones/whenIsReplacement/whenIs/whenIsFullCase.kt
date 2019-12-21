package standalones.whenIsReplacement.whenIs

abstract class SendMessageResponse
class SuccessSendMessageResponse(val messageId: Long) : SendMessageResponse()
class ErrorSendMessageResponse(val errorCode: Int, val message: String) : SendMessageResponse()

class BusinessLogic {
    fun handleResponse(response: SendMessageResponse) {
        when (response) {
            is SuccessSendMessageResponse -> handleSuccess(response.messageId)
            is ErrorSendMessageResponse -> handleError(response.errorCode, response.message)
        }
    }

    private fun handleSuccess(messageId: Long) = println("successfully sent message with id = $messageId")
    private fun handleError(errorCode: Int, message: String) = println("Error no $errorCode. $message")
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