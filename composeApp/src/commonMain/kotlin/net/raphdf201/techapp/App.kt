package net.raphdf201.techapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

/**
 * The main composable function for the application
 */
@Composable
fun App(token: String = "") {
    MaterialTheme {
        if (!init) init()
        val coroutineScope = rememberCoroutineScope()
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
                Spacer(Modifier.height(50.dp))
                AnimatedVisibility(!tokenValid) {
                    login(coroutineScope, uriHandler, textColor)
                }
                AnimatedVisibility(tokenValid) {
                    Column(
                        modifier(10)
                    ) {
                        Spacer(Modifier.height(10.dp))
                        if (eventsList.isNotEmpty()) {
                            events(coroutineScope, textColor, backgroundColor)
                        } else {
                            Text("Aucun évènement", color = textColor)
                        }
                    }
                }
                Button({
                    refresh(coroutineScope)
                }, Modifier) {
                    Text("Refresh", Modifier, textColor)
                }
            }
        }
        if (eventsText != "" && eventsText != unauthorized) {
            eventsList = jsonDecoder.decodeFromString(eventsText)
        } else if (eventsText == unauthorized) {
            tokenValid = false
        } else if (eventsText == "") {
            refresh(coroutineScope)
        }
    }
}
