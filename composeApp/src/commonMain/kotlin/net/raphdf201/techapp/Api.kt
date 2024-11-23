package net.raphdf201.techapp

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.URLProtocol
import io.ktor.http.path

const val techApiHost = "api.team3990.com"
const val bearer = "Bearer "

suspend fun fetchEventsText(client: HttpClient, token: String): String {
    return client.get {
        url {
            protocol = URLProtocol.HTTPS
            host = techApiHost
            path("events/future/with-attendance")
            parameters.append("limit", "50")
            parameters.append("skip", "0")
        }
        headers {
            append(Authorization, token)
        }
    }.bodyAsText()
}

suspend fun fetchGoogle(client: HttpClient): String {
    return client.get("https://api.team3990.com/auth/google").headers["Location"].toString()
}

fun processToken(token: String): String {
    return if (token.contains(bearer)) token
    else bearer + token
}

suspend fun validateToken(client: HttpClient, token: String): Boolean {
    return client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = techApiHost
                path("auth/validate")
            }
            headers {
                append(Authorization, processToken(token))
            }
        }.bodyAsText().split(":")[1].split("}")[0].toBoolean()
}
