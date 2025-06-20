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
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
fun App() {
    MaterialTheme {
        val client by remember { mutableStateOf(HttpClient()) }
        val dark = isSystemInDarkTheme()
        val uriHandler = LocalUriHandler.current
        val backgroundColor: Color
        val textColor: Color
        var eventsText by remember { mutableStateOf("") }
        var eventsList by remember { mutableStateOf(listOf<Event>()) }
        var me by remember { mutableStateOf(listOf<User>()) }
        var tokenValid by remember { mutableStateOf(false) }
        var tokens by remember { mutableStateOf(getTokens()) }
        var init by remember { mutableStateOf(false) }
        var counter by remember { mutableStateOf(0) }

        if (!init) {
            refreshAppInternalTokens = {
                tokens = it
            }
            init = true
        }

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
                Spacer(Modifier.height(50.dp))
                AnimatedVisibility(!tokenValid) {
                    Column(modifier(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Current API : $techApiHost", Modifier, textColor)

                        Button({
                            networkLog("fetching google link")
                            runBlocking {
                                openUri(
                                    uriHandler,
                                    googleLink
                                )
                            }
                        }) {
                            Text("Se connecter", Modifier, textColor)
                        }

                        Button({
                            networkLog("validating token")
                            tokens = Tokens(
                                bearer + tokens.accessToken,
                                bearer + tokens.refreshToken
                            )
                            runBlocking {
                                tokenValid = validateToken(client, tokens.accessToken)
                            }
                            if (tokenValid) storeTokens(tokens)
                            debugLog("token valid : $tokenValid")
                        }) {
                            Text("Évènements", Modifier, textColor)
                        }

                        /*OutlinedTextField(
                            tokens.accessToken,
                            { tokens.accessToken = it },
                            Modifier,
                            label = { Text("Access token", color = textColor) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor,
                                unfocusedBorderColor = textColor
                            )
                        )*/
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
                                                                    client, tokens.accessToken,
                                                                    event, invertAttendance(type)
                                                                )
                                                                eventsText =
                                                                    fetchEventsText(
                                                                        client,
                                                                        tokens.accessToken
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
                    debugLog("old tokens : $tokens")
                    tokens = getTokens()
                    debugLog("new tokens : $tokens")
                    runBlocking {
                        tokenValid = validateToken(client, tokens.accessToken)
                    }
                    debugLog("token valid : $tokenValid")
                    if (tokenValid) {
                        storeTokens(tokens)
                        networkLog("fetching events")
                        runBlocking {
                            eventsText = fetchEventsText(client, tokens.accessToken)
                        }
                        serializationLog("decoding events")
                        try {
                            eventsList = jsonDecoder.decodeFromString(eventsText)
                        } catch (e: Exception) {
                            serializationLog("Error while decoding events error : ${e.message}")
                            serializationLog("Error while decoding events text : $eventsText")
                        }
                    } else if (!areTokensNull(tokens)) {
                        runBlocking {
                            networkLog("refreshing tokens")
                            try {
                                tokens = refreshTokens(client, tokens)
                                storeTokens(tokens)
                                tokenValid = validateToken(client, tokens.accessToken)
                            } catch (e: Exception) {
                                networkLog("failed token refresh : ${e.message}")
                            }
                        }
                    } else debugLog("no tokens")
                }, Modifier) {
                    Text("Actualiser", Modifier, textColor)
                }
                /*Text("access token : ${tokens.accessToken}", Modifier, textColor)
                Text("refresh token : ${tokens.refreshToken}", Modifier, textColor)
                Text("token valid : $tokenValid", Modifier, textColor)*/
            }
        }
        if (tokens.accessToken == "null") {
            tokens = updateAccess(tokens, "")
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
                    eventsText = fetchEventsText(client, tokens.accessToken)
                }
            }
        }
        if (me.isEmpty() && tokenValid) {
            runBlocking {
                serializationLog("decoding user info")
                val userText = "[${fetchUser(client, tokens.accessToken)}]"
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
