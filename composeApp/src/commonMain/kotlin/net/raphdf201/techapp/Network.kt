package net.raphdf201.techapp

import androidx.compose.ui.platform.UriHandler
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

const val techApiHost = "api.team3990.com"
const val present = "present"
const val absent = "absent"
var status: Error = Error.OK
var statusText: String = ""
var requestCount = 0

/**
 *  Fetches the events from the API and returns them as a JSON string
 *  @param client the ktor client to use
 *  @param token the bearer token to use, provided by the [auth/google](api.team3990.com/auth/google) endpoint
 */
suspend fun fetchEventsText(client: HttpClient, token: String): String {
    requestCount++
    val resp = client.get {
        url {
            protocol = URLProtocol.HTTPS
            host = techApiHost
            path("events/future/with-attendance")
            parameters.append("limit", "10")
            parameters.append("skip", "0")
        }
        headers {
            append(Authorization, token)
        }
    }
    statusText = resp.status.toString()
    return resp.bodyAsText()
}

fun fetchEvents(client: HttpClient, decoder: Json, coroutineScope: CoroutineScope, token: String): List<Event> {
    var eventsText: String = ""
    var events: List<Event>
    coroutineScope.launch {
        eventsText = fetchEventsText(client, token)
    }
    try {
        events = decoder.decodeFromString(eventsText)
    } catch (e: Exception) {
        events = listOf(
            Event(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        )
        status = Error.DECODE
        statusText = e.message.toString()
    }
    return events
}

/**
 *  Fetches the google link from the API and returns it
 *  @param client the ktor client to use
 */
suspend fun fetchGoogle(client: HttpClient): String {
    requestCount++
    return client.get {
        url {
            protocol = URLProtocol.HTTPS
            host = techApiHost
            path("auth/google")
        }
    }.headers["Location"].toString()
}

/**
 * Upload the new attendance status to the server
 */
suspend fun changeAttendance(client: HttpClient, token: String, event: Event, attendance: String): String {
    return if (attendance == absent || attendance == present) {
        requestCount++
        client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = techApiHost
                path("events/attendance")
            }
            headers {
                append(Authorization, token)
            }
            setBody("{\"eventId\":${event.id},\"from\":\"${event.beginDate}\",\"to\":\"${event.endDate}\",\"type\":\"$status\"}")
        }.status.toString()
    } else {
        status = Error.ATTENDANCE
        ""
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
        else -> ""
    }
}

/**
 * Validates the token using the [auth/validate](api.team3990.com/auth/validate) endpoint
 */
suspend fun validateToken(client: HttpClient, token: String): Boolean {
    requestCount++
    val resp = client.post {
        url {
            protocol = URLProtocol.HTTPS
            host = techApiHost
            path("auth/validate")
        }
        headers {
            append(Authorization, token)
        }
    }
    statusText = resp.status.toString()
    return resp.bodyAsText().split(":")[1].split("}")[0].toBoolean()
}

/*
/**
 * Refresh the token using the [auth/refresh](api.team3990.com/auth/refresh) endpoint
 */
suspend fun refreshToken(client: HttpClient, token: String): String {
    requestCount++
    return "Bearer " + client.post {
        url {
            protocol = URLProtocol.HTTPS
            host = techApiHost
            path("auth/refresh")
        }
        headers {
            append(Authorization, token)
        }
    }.bodyAsText().split(":")[1].split("\"")[0]
} */

/**
 * Uses the [UriHandler] to open an URL in the default browser
 */
fun openUri(handler: UriHandler, uri: String) {
    try {
        handler.openUri(uri)
    } catch (e: Exception) {
        statusText = e.message.toString()
        status = Error.URIHANDLER
    }
}

/**
 * Possible error types
 */
enum class Error(code: Int = 0) {
    /**
     * No error, default code = 0
     */
    OK(),

    /**
     * Decode error, when serializing events
     */
    DECODE(1),

    /**
     * Network error, when performing network requests
     */
    NETWORK(2),

    /**
     * [UriHandler] error, when opening the google link in the default browser
     */
    URIHANDLER(3),

    /**
     * [Attendance.type] error, when not [present] or [absent]
     */
    ATTENDANCE(4),
}
