package net.raphdf201.techapp

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() {
    ComposeUIViewController { App() }
}

actual fun log(message: String, tag: String) {
}
