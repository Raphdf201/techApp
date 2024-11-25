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
    return client.get {
        url {
            protocol = URLProtocol.HTTPS
            host = techApiHost
            path("auth/google")
        }
    }.headers["Location"].toString()
}

suspend fun validateToken(client: HttpClient, token: String): Boolean {
    return client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = techApiHost
                path("auth/validate")
            }
            headers {
                append(Authorization, token)
            }
        }.bodyAsText().split(":")[1].split("}")[0].toBoolean()
}

suspend fun refreshToken(client: HttpClient, token: String): String {
    return bearer + client.post {
        url {
            protocol = URLProtocol.HTTPS
            host = techApiHost
            path("auth/refresh")
        }
        headers {
            append(Authorization, token)
        }
    }.bodyAsText().split(":")[1].split("\"")[0]
}
