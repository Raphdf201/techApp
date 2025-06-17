package net.raphdf201.techapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.architect.kmpessentials.KmpAndroid
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import logcat.LogPriority.INFO
import logcat.LogPriority.VERBOSE
import logcat.logcat

class MainActivity : FragmentActivity() {
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
        delegate = this.getSharedPreferences("techApp", Context.MODE_PRIVATE)
        KmpAndroid.initializeApp(this) {}
        AndroidLogcatLogger.installOnDebuggableApp(this.application, minPriority = VERBOSE)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let {
            val received = Tokens(
                it.getQueryParameter(access).orEmpty(),
                it.getQueryParameter(refresh).orEmpty()
            )
            storeTokens(received)
            debugLog("received tokens : $received")
            refreshAppInternalTokens()
            debugLog("refreshed app internal tokens")
        }
    }
}

actual fun log(message: String, tag: String) {
    logcat(tag, INFO, message)
}

lateinit var delegate: SharedPreferences

actual val settings: Settings by lazy { SharedPreferencesSettings(delegate) }

fun logcat(tag: String, priority: LogPriority, message: String) {
    logcat(tag, priority) { message }
}
