package net.raphdf201.techapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch

import kotlinx.serialization.json.Json

import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun App() {
    MaterialTheme {
        var googleLink by remember { mutableStateOf("") }
        var googleLoggedIn by remember { mutableStateOf(false) }
        var eventsText by remember { mutableStateOf("") }
        var eventsList by remember { mutableStateOf(listOf<Event>()) }
        var token by remember { mutableStateOf("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjM1LCJlbWFpbCI6ImRlc2NoZW5lcy5yYXBoYWVsQGNyYS5lZHVjYXRpb24iLCJyb2xlIjoiZWxldmUiLCJlcXVpcGUiOiJUSiIsImlhdCI6MTczMjQ1NzMxMywiZXhwIjoxNzMyNDU4MjEzfQ.QkFdRd6MmNhPgGrrMMksGrJf4aEnO_fKmeRbNC91s70") }
        var tokenValid by remember { mutableStateOf(false) }
        val corouScope = rememberCoroutineScope()
        val googleClient = HttpClient { followRedirects = false }
        val uriHandler = LocalUriHandler.current
        val jsonClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                })
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
                        Button(onClick = { corouScope.launch { googleLink = fetchGoogle(googleClient) }; openUri(uriHandler, googleLink) }) {
                            Text("Se connecter avec Google")
                        }
                        OutlinedTextField(
                            value = token,
                            onValueChange = { token = it },
                            label = { Text("Token", color = textColor) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(textColor = textColor, unfocusedBorderColor = textColor)
                        )
                        Button(onClick = { corouScope.launch { tokenValid = validateToken(jsonClient, token) } }) {
                            Text("Valider le token")
                        }
                        Button(onClick = { corouScope.launch { token = refreshToken(jsonClient, token) } }) {
                            Text("Refresher le token")
                        }
                        Button(onClick = { corouScope.launch { eventsText = fetchEventsText(jsonClient, token) } }) {
                            Text("Afficher les évènements")
                        }
                    }
                }
            }
        }
        if (eventsText != "" && eventsText != "{\"message\":\"Unauthorized\",\"statusCode\":401}") {
            eventsList = Json.decodeFromString(eventsText)
        } else if (eventsText == "{\"message\":\"Unauthorized\",\"statusCode\":401}") {
            tokenValid = false
        }
        if (eventsList.isNotEmpty()) {
            Text("net.raphdf201.techapp.Event name : ${eventsList[0].name}")
        }
    }
}

fun openUri(handler: UriHandler, uri: String) {
    try {
        handler.openUri(uri)
    } catch (_: IllegalArgumentException) {
    }
}
