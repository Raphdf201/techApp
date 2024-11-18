package net.raphdf201.techapp

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

suspend fun fetchEvents(client: HttpClient): String {
    val resp: HttpResponse = client.get("https://api.team3990.com/with-attendance")
    val text = resp.bodyAsText()
    return text
    // val data: Event = Json.decodeFromString(jsonString)
    // return data.id.toString()
}
