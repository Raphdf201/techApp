package net.raphdf201.techapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore by preferencesDataStore("prefs")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = intent?.data?.let { uri ->
            uri.getQueryParameter("token") ?: ""
        } ?: ""
        setContent {
            App(remember { dataStore }, token)
        }
    }
}
