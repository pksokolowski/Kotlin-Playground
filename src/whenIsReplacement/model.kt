package whenIsReplacement

abstract class BaseClass{
    abstract fun react(interactor: Interactor)
    abstract fun handle(obj: WhenIsReplacementExpo)
}

interface Interactor{
    fun performSomeActionAlpha(text: String)
    fun performSomeActionBeta(number: Int)
}

class SubClassA(val text: String) : BaseClass(){
    override fun react(interactor: Interactor) {
        interactor.performSomeActionAlpha(text)
    }

    override fun handle(obj: WhenIsReplacementExpo) {
        obj.performActionAlpha(text)
    }
}

class SubClassB(val number: Int) : BaseClass(){
    override fun react(interactor: Interactor) {
        interactor.performSomeActionBeta(number)
    }

    override fun handle(obj: WhenIsReplacementExpo) {
        obj.performActionBeta(number)
    }
}