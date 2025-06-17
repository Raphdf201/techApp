package net.raphdf201.techapp

import androidx.compose.ui.window.ComposeUIViewController
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

fun MainViewController() = ComposeUIViewController { App() }

actual fun log(message: String, tag: String) {}
actual val settings: Settings = NSUserDefaultsSettings(NSUserDefaults())
