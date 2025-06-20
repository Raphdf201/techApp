package net.raphdf201.techapp

import androidx.compose.ui.platform.UriHandler
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.contentType
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 *  Fetches the events from the API and returns them as a JSON string
 *  @param client the ktor client to use
 *  @param token the bearer token to use, provided by the [auth/google](api.team3990.com/auth/google) endpoint
 */
suspend fun fetchEventsText(client: HttpClient, token: String): String {
    val url = "$techApiHost/events/future/with-attendance"
    networkLog("GET $url")
    return client.get(url) {
        url {
            parameters.append("limit", "10")
            parameters.append("skip", "0")
        }
        headers {
            append(Authorization, token)
        }
    }.bodyAsText()
}

suspend fun fetchUser(client: HttpClient, token: String): String {
    val url = "$techApiHost/users/me"
    networkLog("GET $url")
    return try {
        client.get(url) {
            headers {
                append(Authorization, token)
            }
        }.bodyAsText()
    } catch (e: Exception) {
        exceptionLog(e)
        unauthorized
    }
}

/**
 * Upload the new attendance status to the server
 */
suspend fun changeAttendance(
    client: HttpClient,
    token: String,
    event: Event,
    status: String
): String {
    val url = "$techApiHost/events/attendance"
    networkLog("POST $url")
    return try {
        client.post(url) {
            headers {
                append(Authorization, token)
            }
            contentType(ContentType.Application.Json)
            setBody("{\"from\":\"${event.beginDate}\",\"to\":\"${event.endDate}\",\"type\":\"$status\",\"eventId\":${event.id}}")
        }.status.toString()
    } catch (e: Exception) {
        exceptionLog(e)
        waiting
    }
}

/**
 * Inverts the attendance type
 *
 * - absent -> present
 * - present -> absent
 *
 * @param attendance the attendance type to invert
 */
fun invertAttendance(attendance: String): String {
    return when (attendance) {
        absent -> present
        present -> absent
        else -> waiting
    }
}

/**
 * Validates the token using the [auth/validate](api.team3990.com/auth/validate) endpoint
 */
suspend fun validateToken(client: HttpClient, token: String): Boolean {
    val url = "$techApiHost/auth/validate"
    networkLog("POST $url")
    return try {
        client.post(url) {
            headers {
                append(Authorization, token)
            }
        }.bodyAsText().split(":")[1].split("}")[0].toBoolean()
    } catch (e: Exception) {
        exceptionLog(e)
        false
    }
}

suspend fun refreshTokens(client: HttpClient, tokens: Tokens): Tokens {
    val url = "$techApiHost/auth/refresh"
    networkLog("POST $url")
    val resp = client.post(url) {
        headers {
            append(Authorization, tokens.accessToken)
        }
        setBody("{\"refreshToken\":\"${tokens.refreshToken}\"}")
    }
    return try {
        resp.body()
    } catch (e: Exception) {
        exceptionLog(e)
        networkLog("refresh error : ${resp.bodyAsText()}")
        tokens
    }
}

/**
 * Uses the [UriHandler] to open an URL in the default browser
 */
fun openUri(handler: UriHandler, uri: String) {
    try {
        handler.openUri(uri)
    } catch (e: Exception) {
        exceptionLog(e)
    }
}

fun getDate(date: String): Instant = Instant.parse(date.replaceFirst(" ", "T"))

fun getReadableDate(date: Instant): String {
    val local = date.toLocalDateTime(TimeZone.of("UTC-4"))
    return local.date.toString() + " " + local.time
}
