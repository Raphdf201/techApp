package net.raphdf201.techapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun init() {
    init = true
}

fun refresh(coroutineScope: CoroutineScope) {
    if (tokenValid) {
        coroutineScope.launch {
            eventsText = fetchEventsText(client, accessToken)
        }
        eventsList = jsonDecoder.decodeFromString(eventsText)
    }
}