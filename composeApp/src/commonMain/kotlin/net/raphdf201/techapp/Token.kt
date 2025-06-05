package net.raphdf201.techapp

fun registerTokens(access: String?, refresh: String?) {
    if (!access.isNullOrBlank()) accessToken = access
    if (!refresh.isNullOrBlank()) refreshToken = refresh
}

var accessToken = ""
var refreshToken = ""
