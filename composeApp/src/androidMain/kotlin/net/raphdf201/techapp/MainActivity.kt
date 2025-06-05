package net.raphdf201.techapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.architect.kmpessentials.KmpAndroid
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
        KmpAndroid.initializeApp(this) {}
        AndroidLogcatLogger.installOnDebuggableApp(this.application, minPriority = VERBOSE)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let {
            registerTokens(
                it.getQueryParameter("access_token"),
                it.getQueryParameter("refresh_token")
            )
        }
    }
}

actual fun log(message: String, tag: String) {
    logcat(tag, INFO, message)
}

fun logcat(tag: String, priority: LogPriority, message: String) {
    logcat(tag, priority) { message }
}
