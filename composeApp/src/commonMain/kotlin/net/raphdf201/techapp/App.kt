package net.raphdf201.techapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import io.ktor.util.logging.Logger
import kotlinx.coroutines.launch
import net.raphdf201.techapp.network.changeAttendance
import net.raphdf201.techapp.network.fetchEventsText
import net.raphdf201.techapp.network.fetchGoogle
import net.raphdf201.techapp.network.invertAttendance
import net.raphdf201.techapp.network.openUri
import net.raphdf201.techapp.network.result
import net.raphdf201.techapp.network.validateToken
import net.raphdf201.techapp.vals.Event
import net.raphdf201.techapp.vals.absent
import net.raphdf201.techapp.vals.grey
import net.raphdf201.techapp.vals.jsonClient
import net.raphdf201.techapp.vals.jsonDecoder
import net.raphdf201.techapp.vals.modifier
import net.raphdf201.techapp.vals.present

/**
 * The main composable function for the application
 */
@Composable
fun App() {
    MaterialTheme {
        var eventsText by remember { mutableStateOf("") }
        var eventsList by remember { mutableStateOf(listOf<Event>()) }
        var token by remember { mutableStateOf("") }
        var tokenValid by remember { mutableStateOf(false) }
        val corouScope = rememberCoroutineScope()
        val dark = isSystemInDarkTheme()
        val uriHandler = LocalUriHandler.current
        val backgroundColor: Color
        val textColor: Color
        if (dark) {
            backgroundColor = grey
            textColor = Color.White
        } else {
            backgroundColor = Color.White
            textColor = Color.Black
        }

        Surface(
            Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            Column(modifier(), horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedVisibility(!tokenValid) {
                    Column(
                        modifier(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button({
                            corouScope.launch {
                                openUri(
                                    uriHandler,
                                    fetchGoogle(HttpClient { followRedirects = false })
                                )
                            }
                        }) {
                            Text("Accéder au site", Modifier, textColor)
                        }
                        Button({
                            corouScope.launch {
                                tokenValid = validateToken(jsonClient, token)
                            }
                        }) {
                            Text("Se connecter", Modifier, textColor)
                        }
                        /* Button({ corouScope.launch { token = refreshToken(jsonClient, token) } }) {
                        Text("Regénérer le token")
                    } */
                        OutlinedTextField(
                            // Modifier,
                            token,
                            { token = it },
                            label = { Text("Token", color = textColor) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = textColor,
                                unfocusedBorderColor = textColor
                            )
                        )
                        Text("Result : $result", Modifier, textColor)
                    }
                }
                AnimatedVisibility(tokenValid) {
                    Column(
                        modifier(10)
                    ) {
                        Spacer(Modifier.height(10.dp))
                        if (eventsList.isNotEmpty()) {
                            LazyColumn(modifier()) {
                                items(eventsList.chunked(eventsList.size)) { columnEvents ->
                                    columnEvents.forEach { event ->
                                        Row(
                                            modifier(8, 2, textColor),
                                            Arrangement.SpaceEvenly,
                                            Alignment.CenterVertically
                                        ) {
                                            event.name?.let { Text(it, Modifier, textColor) }
                                            event.attendance?.getOrNull(0)?.type?.let { type ->
                                                val buttonColor: ButtonColors = when (type) {
                                                    present -> ButtonDefaults.buttonColors(Color.Green)
                                                    absent -> ButtonDefaults.buttonColors(Color.Red)
                                                    else -> ButtonDefaults.buttonColors(Color.Yellow)
                                                }
                                                Button(
                                                    {
                                                        corouScope.launch {
                                                            event.id?.let { eventId ->
                                                                changeAttendance(
                                                                    jsonClient, token,
                                                                    eventId, invertAttendance(type)
                                                                )
                                                            }
                                                        }
                                                    },
                                                    // Modifier,
                                                    colors = buttonColor
                                                ) { Text(type, Modifier, backgroundColor) }
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
