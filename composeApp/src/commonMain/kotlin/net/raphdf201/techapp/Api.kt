package net.raphdf201.techapp

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

suspend fun fetchEvents(client: HttpClient): String {
    val resp: HttpResponse = client.get("https://api.team3990.com/events/future/with-attendance") {
        url {
            parameters.append("limit", "50")
            parameters.append("skip", "0")
        }
    }
    return ""
}

suspend fun fetchGoogle(client: HttpClient): String {
    return client.get("https://api.team3990.com/auth/google").headers["Location"].toString()
}
