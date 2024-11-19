package net.raphdf201.techapp

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
data class Creator(                 // SUB 1
    val id: Int,
    val name: String
)

@Serializable
data class Event(               // BASE
    val id: Int,
    val name: String,
    val description: String,
    val location: String,
    val beginDate: String,
    val endDate: String,
    val status: EventStatus,
    val team: Team,
    val maxParticipants: Int,
    val creator: Creator,
    val attendance: AttendanceObject,
    val invitations: InvitationObject,
    val full: Boolean,
    val userAttendance: AttendanceObject
)
