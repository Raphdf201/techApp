package net.raphdf201.techapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import net.raphdf201.techapp.storage.DataStoreWrapper

private val Context.dataStore by preferencesDataStore("settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Composable
fun AppAndroidPreview() {
    App()
}

class AndroidDataStoreWrapper(context: Context) : DataStoreWrapper {
    private val dataStore = context.dataStore

    override suspend fun saveData(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getData(key: String): String? {
        val preferencesKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[preferencesKey]
    }
}
