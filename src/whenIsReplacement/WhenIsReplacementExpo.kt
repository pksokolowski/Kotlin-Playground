package whenIsReplacement

import Expo

/**
 * While differentiating explicitly between classes and acting differently based on which type was found
 * is considered a code smell, for a case of communication between two parties, say api and an app,
 * this might be a very convenient way, particularly in Kotlin, of handling polymorphic responses
 * which arrive with various sets of data. For example an api could respond with either the exact answer
 * requested, or a prerequisite demand including either the requested properties or properties needed
 * to identify the data requirements still unmet but necessary to be provided to the api prior to
 * being authorized to obtain the requested response.
 *
 * A when-is solution is applied along with a replacement candidate. The candidate seems wanting in
 * conciseness and clarity. Also it loses the point of open closed principle as each new way to interact
 * with the affected class has to be implemented both in the class and then in the new response class, and
 * in the meantime - declared in an interface.
 *
 * The when-is solution seems better for this kind of communication scenario, as much as it doesn't
 * fill the bill in most other scenarios, falling short of the beauty of proper polymorphism.
 */
object WhenIsReplacementExpo : Expo {
    override fun runSample() {
        val input = listOf(SubClassA("d"), SubCLassB(4))

        runTheWhenIsSolution(input)
        runTheReplacementCandidate(input)
    }

    private fun runTheWhenIsSolution(input: List<BaseClass>) {
        input.forEach { case ->
            when (case) {
                is SubClassA -> {
                    performActionAlpha(case.text)
                }
                is SubCLassB -> {
                    performActionBeta(case.number)
                }
            }
        }
    }

    private fun runTheReplacementCandidate(input: List<BaseClass>) {
        val interactor = object : Interactor {
            override fun performSomeActionAlpha(text: String) {
                performActionAlpha(text)
            }

            override fun performSomeActionBeta(number: Int) {
                performActionBeta(number)
            }

        }
        input.forEach { case ->
            case.react(interactor)
        }
    }

    fun performActionAlpha(text: String) = println("Action Alpha is being performed. Param is $text")
    fun performActionBeta(number: Int) = println("Action Beta is being performed. Param is $number")
}