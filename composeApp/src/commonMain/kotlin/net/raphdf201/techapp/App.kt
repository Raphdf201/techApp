package net.raphdf201.techapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

import kotlinx.serialization.json.Json

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var textDisp by remember { mutableStateOf<String>("") }
        var loginGoogle by remember { mutableStateOf(false) }
        var token by remember { mutableStateOf<String>("") }
        val uriHandler = LocalUriHandler.current
        val jsonClient = HttpClient() {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                })
            }
        }
        val noRedirClient = HttpClient() { followRedirects = false }
        val finalColor: Color = if (isSystemInDarkTheme()) {
            Color(0, 0, 0)
        } else {
            Color(255, 255, 255)
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = finalColor
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedVisibility(!loginGoogle) {
                    Button(onClick = { loginGoogle = true }) {
                        Text("Se connecter avec Google")
                    }
                }
            }
            LaunchedEffect(key1 = Unit) {
                textDisp = fetchGoogle(noRedirClient)
            }
            if (loginGoogle) {
                try {
                    uriHandler.openUri(textDisp)
                } catch (_: IllegalArgumentException) {
                }
            }
        }
    }
}
