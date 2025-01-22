package net.raphdf201.techapp.vals

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

import kotlinx.serialization.json.Json

val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            useAlternativeNames = false
        })
    }
}

val jsonDecoder = Json {
    ignoreUnknownKeys = true
}
