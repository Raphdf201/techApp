package net.raphdf201.techapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.collectAsState
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.raphdf201.techapp.network.changeAttendance
import net.raphdf201.techapp.network.fetchEventsText
import net.raphdf201.techapp.network.fetchGoogle
import net.raphdf201.techapp.network.invertAttendance
import net.raphdf201.techapp.network.netStatus
import net.raphdf201.techapp.network.requestCount
import net.raphdf201.techapp.network.openUri
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
fun App(prefs: DataStore<Preferences>, inputToken: String) {
    MaterialTheme {
        val tokenKey by remember { mutableStateOf(stringPreferencesKey("token")) }
        var token by remember { mutableStateOf("") }
        val prefToken by prefs
            .data
            .map {
                it[tokenKey] ?: ""
            }
            .collectAsState("")
        if (inputToken != "") {
            token = inputToken
        } else if (prefToken != "") {
            token = prefToken
        }
        var eventsText by remember { mutableStateOf("") }
        var eventsList by remember { mutableStateOf(listOf<Event>()) }
        var tokenValid by remember { mutableStateOf(false) }
        var fetchError by remember { mutableStateOf("") }
        var decodeError by remember { mutableStateOf("") }
        var netResponse by remember { mutableStateOf("") }
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
                            val cli = HttpClient { followRedirects = false }
                            requestCount++
                            corouScope.launch {
                                openUri(
                                    uriHandler,
                                    fetchGoogle(cli)
                                )
                            }
                            cli.close()
                        }) {
                            Text("Accéder au site", Modifier, textColor)
                        }
                        Button({
                            requestCount++
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
                        Text(netStatus, Modifier, textColor)
                        Text(fetchError, Modifier, textColor)
                        Text(decodeError, Modifier, textColor)
                        Text(requestCount.toString(), Modifier, textColor)
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
                                            event.name?.let {
                                                Text(
                                                    it,
                                                    Modifier.padding(8.dp),
                                                    textColor
                                                )
                                            }
                                            event.attendance?.getOrNull(0)?.type?.let { type ->
                                                val buttonColor: ButtonColors = when (type) {
                                                    present -> ButtonDefaults.buttonColors(Color.Green)
                                                    absent -> ButtonDefaults.buttonColors(Color.Red)
                                                    else -> ButtonDefaults.buttonColors(Color.Yellow)
                                                }
                                                Button(
                                                    {
                                                        requestCount++
                                                        corouScope.launch {
                                                            netResponse = changeAttendance(
                                                                jsonClient, token,
                                                                event, invertAttendance(type)
                                                            )

                                                        }
                                                    },
                                                    Modifier.padding(1.dp),
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
                        Text("resp : $netResponse", Modifier, textColor)
                        Text("fetch : $fetchError", Modifier, textColor)
                        Text("deco : $decodeError", Modifier, textColor)
                        Text("reqs : $requestCount", Modifier, textColor)
                    }
                }
            }
        }
        if (eventsText != "" && eventsText != "{\"message\":\"Unauthorized\",\"statusCode\":401}") {
            try {
                eventsList = jsonDecoder.decodeFromString(eventsText)
            } catch (e: Exception) {
                decodeError = e.message.toString()
            }
        } else if (eventsText == "{\"message\":\"Unauthorized\",\"statusCode\":401}") {
            tokenValid = false
        } else if (eventsText == "") {
            if (tokenValid) {
                requestCount++
                try {
                    corouScope.launch {
                        eventsText = fetchEventsText(jsonClient, token)
                    }
                } catch (e: Exception) {
                    fetchError = e.message.toString()
                }
            }
        }
    }
}
