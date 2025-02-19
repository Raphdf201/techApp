package net.raphdf201.techapp

import androidx.compose.ui.graphics.Color
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * The [HttpClient] used to perform any Json requests
 */
val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            useAlternativeNames = false
        })
    }
}

/**
 * The [Json] parameters used for json serializing
 */
val jsonDecoder = Json {
    ignoreUnknownKeys = true
}

/**
 * The background [Color] in dark mode
 *
 * Triggered by [androidx.compose.foundation.isSystemInDarkTheme]
 */
val grey = Color(30, 31, 34)

/**
 * Json returned when the token is invalid
 */
const val unauthorized = "{\"message\":\"Unauthorized\",\"statusCode\":401}"
