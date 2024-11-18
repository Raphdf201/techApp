package net.raphdf201.techapp

enum class Team {   // TODO : complete
    TJ, BOTH
}

enum class AttendanceType {
    ABSENT, WAITING, PRESENT
}

enum class EventStatus {    // TODO : complete
    ACTIVE
}

enum class Role {   // TODO : complete
    ELEVE
}

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

data class InvitationObject(              // SUB 1
    val id: Int,
    val user: User
)

data class AttendanceObject(        // SUB 1
    val id: Int,
    val type: AttendanceType,
    val beginDate: String,
    val endDate: String,
    val markDate: String,
    val user: User
)

data class Creator(                 // SUB 1
    val id: Int,
    val name: String
)

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