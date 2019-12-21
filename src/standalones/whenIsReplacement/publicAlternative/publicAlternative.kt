package standalones.whenIsReplacement.publicAlternative

abstract class SendMessageResponse {
    abstract fun handle(logic: BusinessLogic)
}

class SuccessSendMessageResponse(private val messageId: Long) : SendMessageResponse() {
    override fun handle(logic: BusinessLogic) = logic.handleSuccess(messageId)
}

class ErrorSendMessageResponse(private val errorCode: Int, private val message: String) : SendMessageResponse() {
    override fun handle(logic: BusinessLogic) = logic.handleError(errorCode, message)
}

class BusinessLogic {
    fun handleResponse(response: SendMessageResponse) {
        response.handle(this)
    }

    fun handleSuccess(messageId: Long) = println("successfully sent message with id = $messageId")
    fun handleError(errorCode: Int, message: String) = println("Error no $errorCode. $message")
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