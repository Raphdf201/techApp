package net.raphdf201.techapp

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.json.Json

/**
 * The [Json] parameters used for json serializing
 */
val jsonDecoder = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

/**
 * The background [Color] in dark mode
 *
 * Triggered by [androidx.compose.foundation.isSystemInDarkTheme]
 */
val grey = Color(30, 31, 34)

const val unauthorized = "{\"message\":\"Unauthorized\",\"statusCode\":401}"
const val absent = "absent"
const val present = "present"
const val waiting = "waiting"
const val techApiHost = "https://raphdf.ddns.net/tech/api"
const val googleLink = "$techApiHost/auth/googleMobile"
const val access = "access_token"
const val refresh = "refresh_token"
const val bearer = "Bearer "

var refreshAppInternalTokens: (Tokens) -> Unit = {}
