package whenIsReplacement

abstract class BaseClass{
    abstract fun react(interactor: Interactor)
}

interface Interactor{
    fun performSomeActionAlpha(text: String)
    fun performSomeActionBeta(number: Int)
}

class SubClassA(val text: String) : BaseClass(){
    override fun react(interactor: Interactor) {
        interactor.performSomeActionAlpha(text)
    }
}

class SubCLassB(val number: Int) : BaseClass(){
    override fun react(interactor: Interactor) {
        interactor.performSomeActionBeta(number)
    }
}