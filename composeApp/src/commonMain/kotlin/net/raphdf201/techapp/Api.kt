package net.raphdf201.techapp

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

suspend fun fetchEvents(client: HttpClient): String {
    val resp: HttpResponse = client.get("https://api.team3990.com/with-attendance")
    val jsonString = resp.body<String>()
    val data: Event = Json.decodeFromString(jsonString)
    return data.id.toString()
}
