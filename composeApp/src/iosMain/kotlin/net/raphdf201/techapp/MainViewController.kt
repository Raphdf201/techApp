package net.raphdf201.techapp

import androidx.compose.ui.window.ComposeUIViewController
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSUserDefaults
import platform.darwin.NSObject

fun MainViewController() = ComposeUIViewController { App() }

interface AuthTokenHandler {
    fun handleAuthCallback(accessToken: String, refreshToken: String)
}

object MyAuthHandler : AuthTokenHandler {
    override fun handleAuthCallback(accessToken: String, refreshToken: String) {
        storeTokens(
            Tokens(
                accessToken,
                refreshToken
            )
        )
        refreshAppInternalTokens()
    }
}

actual fun log(message: String, tag: String) {}
actual val settings: Settings = NSUserDefaultsSettings(NSUserDefaults())

@OptIn(BetaInteropApi::class)
@ExportObjCClass
class AuthBridge : NSObject() {
    @OptIn(BetaInteropApi::class)
    @ObjCAction
    fun handleAuthCallback(accessToken: String, refreshToken: String) {
        MyAuthHandler.handleAuthCallback(accessToken, refreshToken)
    }
}
