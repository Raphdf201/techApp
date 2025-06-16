package net.raphdf201.techapp

import androidx.compose.ui.platform.UriHandler
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.contentType

/**
 *  Fetches the events from the API and returns them as a JSON string
 *  @param client the ktor client to use
 *  @param token the bearer token to use, provided by the [auth/google](api.team3990.com/auth/google) endpoint
 */
suspend fun fetchEventsText(client: HttpClient, token: String): String {
    return client.get("$techApiHost/events/future/with-attendance") {
        url {
            parameters.append("limit", "10")
            parameters.append("skip", "0")
        }
        headers {
            append(Authorization, token)
        }
    }.bodyAsText()
}

/**
 *  Fetches the google link from the API and returns it
 *  @param client the ktor client to use
 */
suspend fun fetchGoogle(client: HttpClient): String {
    return try {
        client.get("$techApiHost/auth/google").headers["Location"].toString()
    } catch (e: Exception) {
        exceptionLog(e)
        "https://raphdf.ddns.net/tech/errors/fetchGoogle.html"
    }
}

suspend fun fetchUser(client: HttpClient, token: String): String {
    return client.get(techApiHost + "users/me") {
        headers {
            append(Authorization, token)
        }
    }.bodyAsText()
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
    return client.post("$techApiHost/events/attendance") {
        headers {
            append(Authorization, token)
        }
        contentType(ContentType.Application.Json)
        setBody("{\"from\":\"${event.beginDate}\",\"to\":\"${event.endDate}\",\"type\":\"$status\",\"eventId\":${event.id}}")
    }.status.toString()
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
        waiting -> waiting
        else -> ""
    }
}

/**
 * Validates the token using the [auth/validate](api.team3990.com/auth/validate) endpoint
 */
suspend fun validateToken(client: HttpClient, token: String): Boolean {
    return try {
        client.post("$techApiHost/auth/validate") {
            headers {
                append(Authorization, token)
            }
        }.bodyAsText().split(":")[1].split("}")[0].toBoolean()
    } catch (e: Exception) {
        exceptionLog(e)
        false
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
