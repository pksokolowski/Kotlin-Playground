package standalones.whenIsReplacement.privateAlternative

import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

abstract class SendMessageResponse {
    abstract fun handle(logic: BusinessLogic)
}

class SuccessSendMessageResponse(private val messageId: Long) : SendMessageResponse() {
    override fun handle(logic: BusinessLogic) {
        logic::class.declaredMemberFunctions.firstOrNull { it.name == "handleSuccess" }
            ?.apply { isAccessible = true }
            ?.call(logic, messageId)
    }
}

class ErrorSendMessageResponse(private val errorCode: Int, private val message: String) : SendMessageResponse() {
    override fun handle(logic: BusinessLogic) {
        logic::class.declaredMemberFunctions.firstOrNull { it.name == "handleError" }
            ?.apply { isAccessible = true }
            ?.call(logic, errorCode, message)
    }
}

class BusinessLogic {
    fun handleResponse(response: SendMessageResponse) {
        response.handle(this)
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