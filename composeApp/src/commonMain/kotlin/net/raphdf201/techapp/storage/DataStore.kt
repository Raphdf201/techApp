package net.raphdf201.techapp.storage

interface DataStoreWrapper {
    suspend fun saveData(key: String, value: String)
    suspend fun getData(key: String): String?
}


