package net.raphdf201.techapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking

/**
 * The main composable function for the application
 */
@Composable
fun App(tkn: String = "") {
    MaterialTheme {
        var eventsText by remember { mutableStateOf("") }
        var eventsList by remember { mutableStateOf(listOf<Event>()) }
        var me by remember { mutableStateOf(listOf<User>()) }
        var tokenValid by remember { mutableStateOf(false) }
        var init by remember { mutableStateOf(false) }
        val client by remember { mutableStateOf(HttpClient()) }
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

        if (!init) {
            accessToken = if (tkn == "") get1() else tkn
            init = true
        }

        Surface(
            Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            Column(modifier(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(50.dp))
                AnimatedVisibility(!tokenValid) {
                    Column(modifier(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Button({
                            networkLog("fetching google link")
                            runBlocking {
                                openUri(
                                    uriHandler,
                                    fetchGoogle(client)
                                )
                            }
                        }) {
                            Text("Accéder au site", Modifier, textColor)
                        }
                        Button({
                            networkLog("validating token")
                            runBlocking {
                                tokenValid = validateToken(client, accessToken)
                            }
                            if (tokenValid) store1(accessToken)
                        }) {
                            Text("Se connecter", Modifier, textColor)
                        }
                        OutlinedTextField(
                            accessToken,
                            { accessToken = it },
                            Modifier,
                            label = { Text("Access token", color = textColor) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor,
                                unfocusedBorderColor = textColor
                            )
                        )
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
                                            Arrangement.SpaceBetween,
                                            Alignment.CenterVertically
                                        ) {
                                            Text(
                                                event.name,
                                                Modifier.padding(8.dp),
                                                textColor
                                            )
                                            event.userAttendance.type.let { type ->
                                                val buttonColor: ButtonColors =
                                                    getButtonColor(me, event)
                                                Button(
                                                    {
                                                        if (tokenValid) {
                                                            networkLog("changing attendance status")
                                                            networkLog("fetching events")
                                                            runBlocking {
                                                                changeAttendance(
                                                                    client, accessToken,
                                                                    event, invertAttendance(type)
                                                                )
                                                                eventsText =
                                                                    fetchEventsText(
                                                                        client,
                                                                        accessToken
                                                                    )
                                                            }
                                                            serializationLog("decoding events")
                                                            try {
                                                                eventsList =
                                                                    jsonDecoder.decodeFromString(
                                                                        eventsText
                                                                    )
                                                            } catch (e: Exception) {
                                                                serializationLog("Error while decoding events error : ${e.message}")
                                                                serializationLog("Error while decoding events text : $eventsText")
                                                            }
                                                        }
                                                    },
                                                    Modifier.padding(1.dp).offset((-6).dp),
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
                Button({
                    if (tokenValid) {
                        networkLog("fetching events")
                        runBlocking {
                            eventsText = fetchEventsText(client, accessToken)
                        }
                        serializationLog("decoding events")
                        try {
                            eventsList = jsonDecoder.decodeFromString(eventsText)
                        } catch (e: Exception) {
                            serializationLog("Error while decoding events error : ${e.message}")
                            serializationLog("Error while decoding events text : $eventsText")
                        }
                    }
                }, Modifier) {
                    Text("Refresh", Modifier, textColor)
                }
            }
        }
        if (accessToken == "null") {
            accessToken = ""
        }
        if (eventsText != "" && eventsText != unauthorized) {
            serializationLog("decoding events")
            try {
                eventsList = jsonDecoder.decodeFromString(eventsText)
            } catch (e: Exception) {
                serializationLog("Error while decoding events error : ${e.message}")
                serializationLog("Error while decoding events text : $eventsText")
            }
        } else if (eventsText == unauthorized) {
            tokenValid = false
        } else if (eventsText == "") {
            if (tokenValid) {
                networkLog("fetching events")
                runBlocking {
                    eventsText = fetchEventsText(client, accessToken)
                }
            }
        }
        if (me.isEmpty() && tokenValid) {
            runBlocking {
                serializationLog("decoding user info")
                val userText = "[${fetchUser(client, accessToken)}]"
                try {
                    me = jsonDecoder.decodeFromString(userText)
                } catch (e: Exception) {
                    serializationLog("Error while decoding user error : ${e.message}")
                    serializationLog("Error while decoding user text : $userText")
                }
            }
        }
    }
}
