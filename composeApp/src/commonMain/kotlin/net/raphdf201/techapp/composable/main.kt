package net.raphdf201.techapp.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.raphdf201.techapp.network.changeAttendance
import net.raphdf201.techapp.network.fetchGoogle
import net.raphdf201.techapp.network.invertAttendance
import net.raphdf201.techapp.network.openUri
import net.raphdf201.techapp.network.validateToken
import net.raphdf201.techapp.vals.absent
import net.raphdf201.techapp.vals.jsonClient
import net.raphdf201.techapp.vals.modifier
import net.raphdf201.techapp.vals.present

@Composable
fun mainApp(backgroundColor: Color) {
    Surface(
        Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        if (tokenValid) {

        } else {
            invalidToken()
        }
    }
}

@Composable
fun invalidToken(
    corouScope: CoroutineScope,
    uriHandler: UriHandler,
    initialTokenValid: Boolean,
    initialToken: String,
    textColor: Color
) {
    var tokenValid = initialTokenValid
    var token = initialToken
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
            Text("Accéder au site")
        }
        Button({
            corouScope.launch {
                tokenValid = validateToken(jsonClient, token)
            }
        }) {
            Text("Se connecter")
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
    }
}

@Composable
fun validToken() {
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
                            verticalAlignment = Alignment.CenterVertically
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
                                ) { Text(type, Modifier, textColor) }
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
