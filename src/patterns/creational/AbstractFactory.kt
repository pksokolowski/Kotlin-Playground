package patterns.creational

abstract class DataSourceFactory {
    abstract fun makeDataSource(): PostDataSource

    companion object {
        inline fun <reified T : PostDataSource> createFactory(): DataSourceFactory {
            return when (T::class) {
                NetworkDataSource::class -> NetworkDataSourceFactory()
                CacheDataSource::class -> CacheDataSourceFactory()
                else -> throw IllegalArgumentException("unknown type")
            }
        }
    }
}

interface PostDataSource {
    fun getData(): List<PostData>
}

data class PostData(val authorId: Long, val content: String)

class NetworkDataSource : PostDataSource {
    override fun getData(): List<PostData> {
        return listOf(PostData(1, "kopkop, updated version"))
    }
}

class CacheDataSource : PostDataSource {
    override fun getData(): List<PostData> {
        return listOf(PostData(1, "kopkop, old version"))
    }
}

class NetworkDataSourceFactory : DataSourceFactory() {
    override fun makeDataSource(): PostDataSource = NetworkDataSource()
}

class CacheDataSourceFactory : DataSourceFactory() {
    override fun makeDataSource(): PostDataSource = CacheDataSource()

}


fun main() {
    val networkFactory = DataSourceFactory.createFactory<NetworkDataSource>()
    val cacheFactory = DataSourceFactory.createFactory<CacheDataSource>()

    val networkDataSource = networkFactory.makeDataSource()
    val cacheDataSource = cacheFactory.makeDataSource()

    val dataFromNetwork = networkDataSource.getData()
    val dataFromCache = cacheDataSource.getData()

    println(
        """
        created factories for:
        network data access: ${networkFactory::class.simpleName}
        \- which provided us DataSource: ${networkDataSource::class.simpleName}
        \-- which offered the following data: $dataFromNetwork
        cached data access:  ${cacheFactory::class.simpleName}
        \- which provided us DataSource: ${cacheDataSource::class.simpleName}
        \-- which offered the following data: $dataFromCache
    """.trimIndent()
    )


}