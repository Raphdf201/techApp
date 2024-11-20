package net.raphdf201.techapp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Team {   // TODO : complete
    TJ, BOTH
}

@Serializable
enum class AttendanceType {
    ABSENT, WAITING, PRESENT
}

@Serializable
enum class EventStatus {    // TODO : complete
    ACTIVE
}

@Serializable
enum class Role {   // TODO : complete
    ELEVE
}

@Serializable
data class User(
    // SUB 2
    val id: Int,
    val name: String,
    val avatar: String,
    val role: Role,
    val team: Team,
    val bio: String,
    val email: String,
)

@Serializable
data class InvitationObject(              // SUB 1
    val id: Int,
    val user: User
)

@Serializable
data class AttendanceObject(        // SUB 1
    val id: Int,
    val type: AttendanceType,
    val beginDate: String,
    val endDate: String,
    val markDate: String,
    val user: User
)

@Serializable
data class UserAttendance(
    // SUB 1
    @SerialName("type")
    val type: AttendanceType,
)

@Serializable
data class Creator(                 // SUB 1
    @SerialName("id")
    val id: Int,
    @SerialName("completeName")
    val name: String
)

@Serializable
data class Event(               // BASE
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("location")
    val location: String,
    @SerialName("beginDate")
    val beginDate: String,
    @SerialName("endDate")
    val endDate: String,
    @SerialName("status")
    // val status: EventStatus,
    val status: String,
    @SerialName("equipe")
    // val team: Team,
    val team: String,
    @SerialName("maxParticipants")
    val maxParticipants: Int,
    @SerialName("full")
    val full: Boolean,
    @SerialName("creator")
    // val creator: Creator,
    val creator: String,
    @SerialName("attendance")
    // val attendance: AttendanceObject,
    val attendance: String,
    @SerialName("invitations")
    // val invitations: InvitationObject,
    val invitations: String,
    @SerialName("userAttendance")
    // val userAttendance: AttendanceObject
    val userAttendance: String
)

data class EventState(
    val isLoading: Boolean = false,
    val launches: List<Event> = emptyList()
)
