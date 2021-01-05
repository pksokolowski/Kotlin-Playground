package patterns.behavioral

abstract class Handler<T : Task<R>, R> {
    protected var nextHandler: Handler<T, R>? = null
    fun addNext(handler: Handler<T, R>): Handler<T, R> {
        nextHandler = handler
        return handler
    }

    abstract fun handle(task: T): R?
}

interface Task<R> {
    fun getFinalResult(): R
}

class EmailSendRequestTask : Task<EmailSendRequest> {
    var flagged = false
    var recipients = ""
    var headers = mutableListOf<String>()
    var content = ""

    override fun getFinalResult(): EmailSendRequest {
        return EmailSendRequest(headers, content)
    }
}

data class EmailSendRequest(val headers: List<String>, val content: String)

class HeadersAddingHandler : Handler<EmailSendRequestTask, EmailSendRequest>() {
    override fun handle(task: EmailSendRequestTask): EmailSendRequest {
        val requiredHeader = "authToken=123"
        if (!task.headers.contains(requiredHeader)) {
            task.headers.add(requiredHeader)
        }
        return nextHandler?.handle(task) ?: task.getFinalResult()
    }
}

class NaiveSignatureAddingHandler : Handler<EmailSendRequestTask, EmailSendRequest>() {
    override fun handle(task: EmailSendRequestTask): EmailSendRequest {
        val requiredSignature = "XYZ"

        if (!task.content.contains(requiredSignature)) {
            task.content = """
                ${task.content}
                
                $requiredSignature
            """.trimIndent()
        }
        return nextHandler?.handle(task) ?: task.getFinalResult()
    }
}

class SecretsProtectionHandler : Handler<EmailSendRequestTask, EmailSendRequest>() {
    private val secrets = listOf("<NewBrandName>", "<UnreleasedProductName>", "replace 4 with A, in the above text")

    override fun handle(task: EmailSendRequestTask): EmailSendRequest {
        if (areThereSecretsIn(task.content)) {
            task.flagged = true
        }
        return nextHandler?.handle(task) ?: task.getFinalResult()
    }

    private fun areThereSecretsIn(content: String) = secrets.any { content.contains(it) }
}

class EmailSendingHandler : Handler<EmailSendRequestTask, EmailSendRequest>() {
    override fun handle(task: EmailSendRequestTask): EmailSendRequest {
        println(
            """
            |EmailSendingHandler has received a task to send the following email:
            |is flagged as containing disclosure of secrets: ${task.flagged}
            |recipients: ${task.recipients}
            |headers: ${task.headers}
            |content: 
            |${task.content}          
        """.trimMargin()
        )
        return nextHandler?.handle(task) ?: task.getFinalResult()
    }

}

class InvalidFormHandler : Handler<EmailSendRequestTask, EmailSendRequest>() {
    override fun handle(task: EmailSendRequestTask): EmailSendRequest? {
        if (task.recipients.isBlank()) {
            println("missing recipient! Aborting.")
            return null
        }
        return nextHandler?.handle(task) ?: task.getFinalResult()
    }
}

class EmailSending

fun main() {
    val formValidityHandler = InvalidFormHandler()
    val headerHandler = HeadersAddingHandler()
    val signatureHandler = NaiveSignatureAddingHandler()
    val secretsHandler = SecretsProtectionHandler()
    val sendingHandler = EmailSendingHandler()

    formValidityHandler
        .addNext(headerHandler)
        .addNext(signatureHandler)
        .addNext(secretsHandler)
        .addNext(sendingHandler)

    listOf<EmailSendRequestTask>(
        EmailSendRequestTask().apply {
            content =
                "Glad to hear that! We will surely take care of it right after we release the <UnreleasedProductName>."
        },
        EmailSendRequestTask().apply {
            recipients = "abc@example.com"
            content =
                "Glad to hear that! We will surely take care of it right after we release the <UnreleasedProductName>."
        },
        EmailSendRequestTask().apply {
            recipients = "abc@example.com"
            content =
                "Glad to hear that! We will take care of it as soon as possible."
        },
    ).forEach { task ->
        println("\n\n------------ beginning of new task ------------")
        formValidityHandler.handle(task)
    }
}