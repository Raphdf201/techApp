package net.raphdf201.techapp.network

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
import net.raphdf201.techapp.vals.Event
import net.raphdf201.techapp.vals.absent
import net.raphdf201.techapp.vals.present
import net.raphdf201.techapp.vals.techApiHost

var netStatus = ""

/**
 *  Fetches the events from the API and returns them as a JSON string
 *  @param client the ktor client to use
 *  @param token the bearer token to use, provided by the [auth/google](api.team3990.com/auth/google) endpoint
 */
suspend fun fetchEventsText(client: HttpClient, token: String): String {
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
    netStatus = resp.status.toString()
    return resp.bodyAsText()
}

/**
 *  Fetches the google link from the API and returns it
 *  @param client the ktor client to use
 */
suspend fun fetchGoogle(client: HttpClient): String {
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
suspend fun changeAttendance(client: HttpClient, token: String, event: Event, status: String) {
    if (status == absent || status == present) {
        client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = techApiHost
                path("events/attendance")
            }
            headers {
                append(Authorization, token)
            }
            setBody("{\"from\":\"${event.beginDate}\",\"to\":\"${event.endDate}\",\"type\":\"$status\",\"eventId\":${event.id}}")
        }
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
    netStatus = resp.status.toString()
    return resp.bodyAsText().split(":")[1].split("}")[0].toBoolean()
}

/*
/**
 * Refresh the token using the [auth/refresh](api.team3990.com/auth/refresh) endpoint
 */
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
} */

/**
 * Uses the [UriHandler] to open an URL in the default browser
 */
fun openUri(handler: UriHandler, uri: String) {
    try {
        handler.openUri(uri)
    } catch (error: IllegalArgumentException) {
        error.printStackTrace()
    }
}
