package net.raphdf201.techapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun App() {
    MaterialTheme {
        var googleLink by remember { mutableStateOf("") }
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
        val stdClient = HttpClient()
        val jsonDecoder = Json {
            ignoreUnknownKeys = true
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
        val eventColumnModifier = dp(8).border(width = 2.dp, color = textColor)
        corouScope.launch { googleLink = fetchGoogle(googleClient) }

        Surface(
            Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedVisibility(!tokenValid) {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = { openUri(uriHandler, googleLink) }) {
                            Text("Accéder au site")
                        }
                        Button(onClick = {
                            corouScope.launch {
                                tokenValid = validateToken(jsonClient, token)
                            }
                        }) {
                            Text("Valider le token")
                        }
                        /* Button(onClick = { corouScope.launch { token = refreshToken(jsonClient, token) } }) {
                            Text("Regénérer le token")
                        } */
                        OutlinedTextField(
                            value = token,
                            onValueChange = { token = it },
                            label = { Text("Token", color = textColor) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = textColor,
                                unfocusedBorderColor = textColor
                            )
                        )
                    }
                }
                AnimatedVisibility(tokenValid) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp)
                    ) {
                        Text("")    // Top padding
                        if (eventsList.isNotEmpty()) {
                            LazyColumn(Modifier.fillMaxWidth()) {
                                items(eventsList.chunked(eventsList.size)) { columnEvents ->
                                    columnEvents.forEach { event ->
                                        Column(
                                            modifier = eventColumnModifier.weight(1f)
                                        ) {
                                            event.name?.let {
                                                Text(
                                                    it,
                                                    Modifier.align(Alignment.Start),
                                                    color = textColor
                                                )
                                            }
                                            event.attendance?.getOrNull(0)?.type?.let {
                                                val buttonColor: ButtonColors = when (it) {
                                                    present -> ButtonDefaults.buttonColors(Color.Green)
                                                    absent -> ButtonDefaults.buttonColors(Color.Red)
                                                    else -> ButtonDefaults.buttonColors(Color.Yellow)
                                                }
                                                Button(
                                                    onClick = {
                                                        corouScope.launch {
                                                            event.id?.let { it1 ->
                                                                changeAttendance(
                                                                    stdClient, token,
                                                                    it1, invertAttendance(it)
                                                                )
                                                            }
                                                        }
                                                    },
                                                    Modifier.align(Alignment.End),
                                                    colors = buttonColor
                                                ) {
                                                    Text(it, color = textColor)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Text("Aucun évènement", color = textColor)
                        }

                    }
                }
            }
        }
        if (eventsText != "" && eventsText != "{\"message\":\"Unauthorized\",\"statusCode\":401}") {
            eventsList = jsonDecoder.decodeFromString(eventsText)
        } else if (eventsText == "{\"message\":\"Unauthorized\",\"statusCode\":401}") {
            tokenValid = false
        }
        if (tokenValid) {
            corouScope.launch {
                eventsText = fetchEventsText(jsonClient, token)
            }
        }
    }
}
