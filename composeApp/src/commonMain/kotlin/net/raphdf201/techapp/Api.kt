package net.raphdf201.techapp

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

suspend fun fetchEvents(client: HttpClient): List<Event> {
    return client.get("https://api.team3990.com/events/future/with-attendance") {
        url {
            parameters.append("limit", "50")
            parameters.append("skip", "0")
        }
    }.body()
}

suspend fun fetchGoogle(client: HttpClient): String {
    return client.get("https://api.team3990.com/auth/google").headers["Location"].toString()
}
