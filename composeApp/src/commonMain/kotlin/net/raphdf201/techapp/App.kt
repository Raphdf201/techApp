package net.raphdf201.techapp

import Event

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
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
import androidx.compose.ui.platform.UriHandler

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

import kotlinx.serialization.json.Json

@Composable
fun App() {
    MaterialTheme {
        var text by remember { mutableStateOf("") }
        var loginGoogle by remember { mutableStateOf(false) }
        var eventsText by remember { mutableStateOf("") }
        var events by remember { mutableStateOf(listOf<Event>()) }
        var token by remember { mutableStateOf("") }
        var tokenValid by remember { mutableStateOf(false) }
        val staticToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjM1LCJlbWFpbCI6ImRlc2NoZW5lcy5yYXBoYWVsQGNyYS5lZHVjYXRpb24iLCJyb2xlIjoiZWxldmUiLCJlcXVpcGUiOiJUSiIsImlhdCI6MTczMjQwNDgyMSwiZXhwIjoxNzMyNDA1NzIxfQ.Q-Uq8n0XSVKqEti0C57VLMqb-RC_t1KhuNXf-7GWWzc"
        val googleClient = HttpClient { followRedirects = false }
        val uriHandler = LocalUriHandler.current
        val jsonClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                })
            }
            Json {
                ignoreUnknownKeys = true
            }
        }
        val backgroundColor: Color
        val textColor: Color
        if (isSystemInDarkTheme()) {
            backgroundColor = Color(26, 28, 29)
            textColor = Color.White
        } else {
            backgroundColor = Color.LightGray
            textColor = Color.Black
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedVisibility(!tokenValid) {
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = { openUri(uriHandler, text) }) {
                            Text("Se connecter avec Google")
                        }
                        OutlinedTextField(
                            value = token,
                            onValueChange = { token = it },
                            label = { Text("Token", color = textColor) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(textColor = textColor, unfocusedBorderColor = textColor)
                        )
                        Button(onClick = {}) {
                            Text("Valider le token")
                        }
                        Button(onClick = {}) {
                            Text("Refresher le token")
                        }
                    }
                }
            }
        }
        LaunchedEffect(key1 = Unit) {
            text = fetchGoogle(googleClient)
            eventsText = fetchEventsText(jsonClient, staticToken)
        }
        if (eventsText != "" && eventsText != "{\"message\":\"Unauthorized\",\"statusCode\":401}") {
            events = Json.decodeFromString(eventsText)
        } else if (eventsText == "{\"message\":\"Unauthorized\",\"statusCode\":401}") {
            tokenValid = false
        }
        if (events.isNotEmpty()) {
            Text("Event name : ${events[0].name}")
        }
    }
}

fun openUri(handler: UriHandler, uri: String) {
    try {
        handler.openUri(uri)
    } catch (_: IllegalArgumentException) {
    }
}
