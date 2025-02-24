package net.raphdf201.techapp

import androidx.compose.ui.graphics.Color
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

const val unauthorized = "{\"message\":\"Unauthorized\",\"statusCode\":401}"
const val absent = "absent"
const val present = "present"
const val bearer = "Bearer "
const val techApiHost = "api.team3990.com"

val jsonDecoder = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

val client = HttpClient()
val grey = Color(30, 31, 34)

var accessToken = ""
var refreshToken = ""
var eventsText = ""
var eventsList = listOf<Event>()
var tokenValid = false
var init = false
