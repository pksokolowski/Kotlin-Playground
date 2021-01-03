package patterns.structural

class Facade(private val hardToUseApi: HardToUseApi, private val storage: Storage) {
    @Synchronized
    fun save(item: Item) {
        hardToUseApi.stageItem(item)
        hardToUseApi.flushItemsTo(storage)
    }

    @Synchronized
    fun readItems() = hardToUseApi.readItems(HardToUseApi.Mode.REAL_DATA, storage)
}

data class Item(val content: String)

interface Storage {
    fun save(item: Item)
    fun readAll(): List<Item>
}

class RamStorage : Storage {
    private val temp = mutableListOf<Item>()
    override fun save(item: Item) {
        temp.add(item)
    }

    override fun readAll(): List<Item> {
        return temp
    }
}

class HardToUseApi {
    private var temp: Item? = null


    fun stageItem(item: Item) {
        temp = item
    }

    fun flushItemsTo(storage: Storage) {
        // the hard to use api introduces a risk of crashes if used without extra caution
        storage.save(temp!!)
    }

    fun readItems(mode: Mode, storage: Storage): List<Item> =
        when (mode) {
            Mode.REAL_DATA -> storage.readAll()
            Mode.FAKE_DATA -> listOf(Item("kopkop"))
        }

    enum class Mode {
        REAL_DATA,
        FAKE_DATA
    }
}

fun main() {
    val hardToUseApi = HardToUseApi()
    val storage = RamStorage()

    val facade = Facade(hardToUseApi, storage)

    println("Saving an Item of 'abc'")
    facade.save(Item("abc"))

    val retrievedItems = facade.readItems()
    println("retrieved the following items: $retrievedItems")

}