package patterns.creational

data class MutableResource(var paramA: Int, var paramB: String) : Cloneable {
    init {
        println(" -- Long running initialization of a resource with parameters: paramA = $paramA, paramB = $paramB -- ")
    }

    public override fun clone(): Any = try {
        super.clone()
    } catch (e: CloneNotSupportedException) {
        println("failed to clone because: $e")
    }
}

data class ResourceParameters(val paramA: Int, val paramB: String)

object ResourceProvider {
    private val cache = hashMapOf<ResourceParameters, MutableResource>()

    fun getMutableResource(params: ResourceParameters): MutableResource {
        val cachedInstance = cache[params] ?: createAndCacheResource(params)
        return cachedInstance.clone() as MutableResource
    }

    private fun createAndCacheResource(params: ResourceParameters) =
        MutableResource(params.paramA, params.paramB).also { cache[params] = it }
}

fun main() {
    val simpleParams = ResourceParameters(123, "abc")

    val simpleRes = ResourceProvider.getMutableResource(simpleParams)
    println("got instance of resource: $simpleRes")

    simpleRes.paramA = 0
    println("modified the resource, now it is: $simpleRes")

    val secondSimpleRes = ResourceProvider.getMutableResource(simpleParams)
    println("got a separate instance with the same params: $secondSimpleRes")

}