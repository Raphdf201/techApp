package net.raphdf201.techapp

import androidx.compose.ui.graphics.Color
import com.architect.kmpessentials.fileSystem.KmpFileSystem
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
const val bearer = "Bearer "
const val techApiHost = "api.team3990.com"
val dir = "${KmpFileSystem.getAppDirectory()}/tech"
val accessTokenDir = "$dir/accessToken.txt"
val refreshTokenDir = "$dir/refreshToken.txt"
