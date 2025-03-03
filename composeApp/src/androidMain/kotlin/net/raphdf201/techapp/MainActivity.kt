package net.raphdf201.techapp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import com.architect.kmpessentials.KmpAndroid

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
    }
}
