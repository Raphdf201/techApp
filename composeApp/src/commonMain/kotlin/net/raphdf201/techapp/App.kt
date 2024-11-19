package net.raphdf201.techapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler

import io.ktor.client.HttpClient

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var textDisp by remember { mutableStateOf<String>("") }
        val client = HttpClient()
        val uriHandler = LocalUriHandler.current
        val finalColor: Color = if (isSystemInDarkTheme()) {
            Color(0, 0, 0)
        } else {
            Color(255, 255, 255)
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = finalColor
        ) {
            LaunchedEffect(key1 = Unit) {
                textDisp = fetchGoogle(client)
            }
            Text(text = "Text : ${textDisp}")
            uriHandler.openUri(textDisp)
        }
    }
}
