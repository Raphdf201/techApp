import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int,
    val name: String,
    val description: String?,
    val location: String,
    val beginDate: String,
    val endDate: String,
    val status: String,
    val equipe: String,
    val maxParticipants: Int?,
    val creator: Creator,
    val attendance: List<Attendance>,
    val invitations: List<Invitation>,
    val full: Boolean,
    val userAttendance: UserAttendance?
)

@Serializable
data class Creator(val id: Int, val completeName: String)

@Serializable
data class Attendance(
    val id: Int,
    val type: String,
    val from: String,
    val to: String,
    val markedAt: String,
    val user: User
)

@Serializable
data class Invitation(val id: Int, val user: User)

@Serializable
data class User(
    val id: Int?,
    val completeName: String?,
    val avatar: String? = null,
    val role: String? = null,
    val equipe: String? = null,
    val bio: String? = null,
    val email: String? = null,
    val createdAt: String? = null,
    val googleId: String? = null,
    val refreshToken: String? = null,
    val profileCompleted: String? = null,
)

@Serializable
data class UserAttendance(
    val id: Int,
    val type: String,
    val from: String,
    val to: String,
    val markedAt: String,
    val user: User
)