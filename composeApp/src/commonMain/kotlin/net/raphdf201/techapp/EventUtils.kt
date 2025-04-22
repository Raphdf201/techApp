package net.raphdf201.techapp

import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

fun userInEvent(user: User, event: Event): Boolean {
    if (event.invitations.isNotEmpty()) {
        for (invitation in event.invitations) {
            if (invitation.user.id == user.id) return true
        }
    }
    return false
}

@Composable
fun getButtonColor(user: List<User>, event: Event): ButtonColors {
    if (user.isNotEmpty()) {
        val type = event.userAttendance.type
        if (userInEvent(user[0], event) && (type == waiting)) {
            return ButtonDefaults.buttonColors(Color.Yellow)
        } else if (type == present) {
            return ButtonDefaults.buttonColors(Color.Green)
        } else if (type == absent) {
            return ButtonDefaults.buttonColors(Color.Red)
        }
    }
    return ButtonDefaults.buttonColors(Color.Gray)
}

@Composable
fun getButtonColor(user: User, event: Event, type: String): ButtonColors {
    if (userInEvent(user, event) && (type == waiting)) {
        return ButtonDefaults.buttonColors(Color.Yellow)
    } else if (type == present) {
        return ButtonDefaults.buttonColors(Color.Green)
    } else if (type == absent) {
        return ButtonDefaults.buttonColors(Color.Red)
    }
    return ButtonDefaults.buttonColors(Color.Gray)
}
