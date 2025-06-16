package net.raphdf201.techapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class UiState {
    object Loading : UiState()
    object Unauthenticated : UiState()
    data class Authenticated(val events: List<Event>, val user: List<User>) : UiState()
    data class Error(val message: String) : UiState()
}

class AppViewModel(initialToken: String) {
    var token by mutableStateOf(initialToken)
    var uiState by mutableStateOf<UiState>(UiState.Loading)
    private val client = HttpClient()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            if (token.isNotBlank()) {
                validateToken()
            } else {
                uiState = UiState.Unauthenticated
            }
        }
    }

    suspend fun validateToken() {
        uiState = UiState.Loading
        val valid = validateToken(client, token)
        if (valid) {
            refreshEvents()
        } else {
            uiState = UiState.Unauthenticated
        }
    }

    suspend fun refreshEvents() {
        uiState = UiState.Loading
        try {
            val events = jsonDecoder.decodeFromString<List<Event>>(fetchEventsText(client, token))
            val userText = fetchUser(client, token)
            val me = jsonDecoder.decodeFromString<List<User>>("[${userText}]")
            uiState = UiState.Authenticated(events, me)
        } catch (e: Exception) {
            exceptionLog(e)
            uiState = UiState.Error("Failed to load events: ${e.message}")
        }
    }

    suspend fun toggleAttendance(event: Event) {
        try {
            changeAttendance(client, token, event, invertAttendance(event.userAttendance.type))
            refreshEvents()
        } catch (e: Exception) {
            exceptionLog(e)
            uiState = UiState.Error("Failed to update attendance: ${e.message}")
        }
    }

    suspend fun fetchGoogleUri(): String? {
        return try {
            fetchGoogle(client)
        } catch (e: Exception) {
            exceptionLog(e)
            null
        }
    }
}

suspend fun CoroutineScope.launchSafely(block: suspend () -> Unit) {
    try {
        withContext(Dispatchers.Default) { block() }
    } catch (e: Exception) {
        exceptionLog(e)
    }
}
