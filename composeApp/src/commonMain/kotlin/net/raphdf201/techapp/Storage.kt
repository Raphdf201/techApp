package net.raphdf201.techapp

import com.russhwolf.settings.Settings

expect val settings: Settings

fun resetTokens(tokens: Tokens) {
    tokens.accessToken = ""
    tokens.refreshToken = ""
}

fun storeTokens(tokens: Tokens) {
    settings.putString(access, tokens.accessToken)
    settings.putString(refresh, tokens.refreshToken)
}

fun getTokens(): Tokens {
    return Tokens(
        settings.getString(access, ""),
        settings.getString(refresh, "")
    )
}
