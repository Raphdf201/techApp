package net.raphdf201.techapp

import com.russhwolf.settings.Settings

expect val settings: Settings

fun resetTokens(): Tokens {
    return Tokens("", "")
}

fun storeTokens(tokens: Tokens) {
    settings.putString(access, tokens.accessToken)
    settings.putString(refresh, tokens.refreshToken)
}

fun areTokensNull(tokens: Tokens): Boolean {
    return tokens.accessToken == "" || tokens.refreshToken == ""
}

fun getTokens(): Tokens {
    return Tokens(
        settings.getString(access, ""),
        settings.getString(refresh, "")
    )
}

fun updateAccess(original: Tokens, newAccess: String): Tokens {
    return Tokens(
        newAccess,
        original.refreshToken
    )
}

fun updateRefresh(original: Tokens, newRefresh: String): Tokens {
    return Tokens(
        original.accessToken,
        newRefresh
    )
}
