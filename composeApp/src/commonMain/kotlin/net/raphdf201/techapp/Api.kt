package net.raphdf201.techapp

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

suspend fun fetchEvents(): String {
    val client = HttpClient() {
    }
    return client.get("https://api.team3990.com/with-attendance").body()