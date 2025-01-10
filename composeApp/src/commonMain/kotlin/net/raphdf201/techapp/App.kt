package net.raphdf201.techapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.Dp
import com.multiplatform.webview.web.WebView

import com.multiplatform.webview.web.rememberWebViewState

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
        var token by remember { mutableStateOf("") }
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
            backgroundColor = Color.White
            textColor = Color.Black
        }
        corouScope.launch { googleLink = fetchGoogle(googleClient) }
        val webViewState = rememberWebViewState(googleLink)
        var launchWebView by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedVisibility(!tokenValid) {
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = { openUri(uriHandler, googleLink) }) {
                            Text("Accéder au site")
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
                            Text("Regénérer le token")
                        }
                        Button(onClick = { launchWebView = true }) {
                            Text("Se connecter")
                        }
                        Button(onClick = { corouScope.launch { eventsText = fetchEventsText(jsonClient, token) } }) {
                            Text("Télécharger les évènements")
                        }
                    }
                }
                AnimatedVisibility(tokenValid) {
                    Column(Modifier.fillMaxWidth().padding(all = Dp(10F)), horizontalAlignment = Alignment.CenterHorizontally) {
                        Column {
                            Text("Event1", color = textColor)
                        }
                        Column {
                            Text("Event2", color = textColor)
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
        if (launchWebView) {
            WebView(webViewState, modifier = Modifier.fillMaxSize())
            launchWebView = false
        }
    }
}
