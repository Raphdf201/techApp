package net.raphdf201.techapp

import com.architect.kmpessentials.fileSystem.KmpFileSystem

var fileStatus = ""

fun getAccessToken():String {
    val res = KmpFileSystem.readTextFromFileAt(accessTokenDir)
    return if (res == null || res == "null") {
        ""
    } else {
        res
    }
}

fun saveAccessToken(token: String) {
    fileStatus = if (KmpFileSystem.writeTextToFileAt(accessTokenDir, token)) {
        "Saved access token"
    } else {
        "Failed to save access token"
    }
}

fun getRefreshToken(): String {
    val res = KmpFileSystem.readTextFromFileAt(refreshTokenDir)
    return if (res == null || res == "null") {
        ""
    } else {
        res
    }
}

fun saveRefreshToken(token: String) {
    fileStatus = if (KmpFileSystem.writeTextToFileAt(refreshTokenDir, token)) {
        "Saved refresh token"
    } else {
        "Failed to save refresh token"
    }
}
