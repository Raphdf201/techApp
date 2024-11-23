package net.raphdf201.techapp

import Event

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

import coil3.compose.rememberAsyncImagePainter

@Composable
fun EventList(events: List<Event>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(events) { event ->
            EventItem(event)
        }
    }
}

@Composable
fun EventItem(event: Event) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        BasicText(text = event.name)
        BasicText(text = "Location: ${event.location}")
        BasicText(text = "Date: ${event.beginDate} - ${event.endDate}")
        BasicText(text = "Created by: ${event.creator.completeName}")
        Spacer(modifier = Modifier.height(8.dp))

        event.userAttendance?.user?.avatar?.let { avatarUrl ->
            Image(
                painter = rememberAsyncImagePainter(avatarUrl),
                contentDescription = "User Avatar",
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
