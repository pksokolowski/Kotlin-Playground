package using

interface Disposable {
    fun dispose()
}

infix fun <T : Disposable> T.useIn(block: T.() -> Unit) = apply(block).dispose()

