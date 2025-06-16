package net.raphdf201.techapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun App(tkn: String = "") {
    val dark = isSystemInDarkTheme()
    val backgroundColor = if (dark) grey else Color.White
    val textColor = if (dark) Color.White else Color.Black
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()

    val viewModel = remember { AppViewModel(tkn) }

    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(50.dp))

            when (val state = viewModel.uiState) {
                is UiState.Loading -> CircularProgressIndicator()

                is UiState.Unauthenticated -> LoginView(
                    viewModel.token,
                    onTokenChange = { viewModel.token = it },
                    onOpenUri = {
                        scope.launch {
                            viewModel.fetchGoogleUri()?.let { uri ->
                                openUri(uriHandler, uri)
                            }
                        }
                    },
                    onLogin = {
                        scope.launch { viewModel.validateToken() }
                    },
                    textColor
                )

                is UiState.Authenticated -> EventListView(
                    state.events,
                    state.user,
                    textColor,
                    backgroundColor,
                    onToggle = { event ->
                        scope.launch {
                            viewModel.toggleAttendance(event)
                        }
                    },
                    onRefresh = {
                        scope.launch { viewModel.refreshEvents() }
                    }
                )

                is UiState.Error -> Text("Error: ${state.message}", color = Color.Red)
            }
        }
    }
}

@Composable
fun LoginView(
    token: String,
    onTokenChange: (String) -> Unit,
    onOpenUri: () -> Unit,
    onLogin: () -> Unit,
    textColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onOpenUri) {
            Text("Accéder au site", color = textColor)
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onLogin) {
            Text("Se connecter", color = textColor)
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = token,
            onValueChange = onTokenChange,
            label = { Text("Access token", color = textColor) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor,
                unfocusedBorderColor = textColor
            )
        )
    }
}

@Composable
fun EventListView(
    events: List<Event>,
    user: List<User>,
    textColor: Color,
    backgroundColor: Color,
    onToggle: (Event) -> Unit,
    onRefresh: () -> Unit
) {
    Column {
        if (events.isEmpty()) {
            Text("Aucun évènement", color = textColor)
        } else {
            LazyColumn {
                items(events) { event ->
                    Row(
                        Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(event.name, color = textColor)
                        Button(
                            onClick = { onToggle(event) },
                            colors = getButtonColor(user, event)
                        ) {
                            Text(event.userAttendance.type, color = backgroundColor)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRefresh) {
            Text("Refresh", color = textColor)
        }
    }
}
