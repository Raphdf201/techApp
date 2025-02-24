package net.raphdf201.techapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import java.io.File

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var instance: MainActivity
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
        instance = this
    }
}

actual class Multi {
    private val file = File(MainActivity.instance.filesDir, "tech")
    actual fun save(string: String) {
        file.writeText(string)
    }

    actual fun load(): String {
        return file.readText()
    }
}