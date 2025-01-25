package net.raphdf201.techapp

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun App() {
    var accessToken by remember { mutableStateOf("") }
    var refreshToken by remember { mutableStateOf("") }
    var events by remember { mutableStateOf(listOf<Event>()) }
    MaterialTheme {

    }
}