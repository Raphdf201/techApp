package net.raphdf201.techapp

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController(token: String) = ComposeUIViewController { App(createDataStoreIos(), token) }
