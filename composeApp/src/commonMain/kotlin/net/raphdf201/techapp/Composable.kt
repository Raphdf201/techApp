package net.raphdf201.techapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun login(coroutineScope: CoroutineScope, uriHandler: UriHandler, textColor: Color) {
    Column(
        modifier(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button({
            coroutineScope.launch {
                openUri(
                    uriHandler,
                    fetchGoogle(client)
                )
            }
        }) {
            Text("Accéder au site", Modifier, textColor)
        }
        Button({
            coroutineScope.launch {
                tokenValid = validateToken(client, accessToken)
            }
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
        OutlinedTextField(
            refreshToken,
            { refreshToken = it },
            Modifier,
            label = { Text("Refresh token", color = textColor) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor,
                unfocusedBorderColor = textColor
            )
        )
    }
}

@Composable
fun events(coroutineScope: CoroutineScope, textColor: Color, backgroundColor: Color) {
    LazyColumn(modifier()) {
        items(eventsList.chunked(eventsList.size)) { columnEvents ->
            columnEvents.forEach { event ->
                Row(
                    modifier(8, 2, textColor),
                    Arrangement.SpaceBetween,
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
                                if (tokenValid) {
                                    coroutineScope.launch {
                                        changeAttendance(
                                            client, accessToken,
                                            event, invertAttendance(type)
                                        )
                                    }
                                    coroutineScope.launch {
                                        eventsText =
                                            fetchEventsText(
                                                client,
                                                accessToken
                                            )
                                    }
                                    eventsList =
                                        jsonDecoder.decodeFromString(
                                            eventsText
                                        )
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
}
