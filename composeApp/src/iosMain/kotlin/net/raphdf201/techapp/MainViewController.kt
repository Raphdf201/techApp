package net.raphdf201.techapp

import androidx.compose.ui.window.ComposeUIViewController
import net.raphdf201.techapp.storage.DataStoreWrapper
import platform.Foundation.NSUserDefaults

fun MainViewController() = ComposeUIViewController { App() }


class IosDataStoreWrapper : DataStoreWrapper {
    private val userDefaults = NSUserDefaults.standardUserDefaults()

    override suspend fun saveData(key: String, value: String) {
        userDefaults.setObject(value, forKey = key)
    }

    override suspend fun getData(key: String): String? {
        return userDefaults.stringForKey(key)
    }
}
