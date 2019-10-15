package nmet

/**
 * Checks whether or not at least n of provided conditions are met.
 * Conditions should be ordered from the most likely to be true as
 * well as from the least expensive to run. Once n conditions are
 * met, no further conditions are checked.
 */
fun atLeastNMet(n: Int, vararg conditions: () -> Boolean): Boolean {
    var metCounter = 0
    var leftToEvaluate = conditions.size - n + 1

    conditions.forEach {
        if (leftToEvaluate-- <= 0 - metCounter) return false
        if (it.invoke()) metCounter++
        if (metCounter == n) return true
    }
    return false
}