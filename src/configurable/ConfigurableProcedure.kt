package configurable

/**
 * holds an arbitrary number of lambdas, all ready to be executed
 * sequentially upon invocation operator call.
 * Could be of use should conditions' evaluation be rather expensive for
 * some of the sub-procedures, so the evaluation could perhaps be
 * done ahead of the time and wait in readiness till the invocation moment.
 */
class ConfigurableProcedure() {
    constructor(vararg tasks: () -> Unit) : this() {
        this.tasks.addAll(tasks)
    }

    private val tasks = mutableListOf<() -> Unit>()
    private val tags = HashMap<String, Int>()

    private object Lock {}

    fun add(task: () -> Unit) {
        synchronized(Lock) {
            tasks.add(task)
        }
    }

    /**
     * @return true if the task with a given tag was added, false if it was already present, and wasn't added.
     */
    fun add(tag: String, task: () -> Unit): Boolean {
        synchronized(Lock) {
            if (tags.contains(tag)) return false
            add(task)
            tags.put(tag, tasks.size - 1)
        }
        return true
    }

    /**
     * @return true if the entry (tag) was there and was deleted, false otherwise
     */
    fun remove(tag: String): Boolean {
        synchronized(Lock) {
            val index = tags[tag] ?: return false
            tags.remove(tag)
            tasks.removeAt(index)
        }
        return true
    }

    operator fun invoke() {
        synchronized(Lock) {
            tasks.forEach {
                it()
            }
        }
    }
}