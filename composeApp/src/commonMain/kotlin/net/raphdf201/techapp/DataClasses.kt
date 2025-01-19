package net.raphdf201.techapp

import kotlinx.serialization.Serializable

val absent = "absent"
val present = "present"

@Serializable
data class Event(
    val id: Int?,
    val name: String?,
    val description: String?,
    val location: String?,
    val beginDate: String?,
    val endDate: String?,
    val status: String?,
    val equipe: String?,
    val maxParticipants: Int?,
    val creator: Creator?,
    val attendance: List<Attendance>?,
    val invitations: List<Invitation>?,
    val full: Boolean?,
    val userAttendance: UserAttendance?
)

@Serializable
data class Creator(val id: Int?, val completeName: String?)

@Serializable
data class Attendance(
    // val id: Int?,
    val type: String?,
    val from: String?,
    val to: String?,
    // val markedAt: String?,
    val user: User?
)

@Serializable
data class Invitation(val id: Int, val user: User)

@Serializable
data class User(
    // val id: Int?,
    val completeName: String?,
    // val avatar: String?,
    // val role: String?,
    // val equipe: String?,
    // val bio: String?,
    // val email: String?,
    // val createdAt: String?,
    // val googleId: String?,
    // val refreshToken: String?,
    // val profileCompleted: Boolean?,
    // val phone: String?,
    // val emergencyPhone: String?,
    // val emergencyRelationship: String?,
    // val hasPassport: Boolean?,
    // val passportExpirationDate: String?,
    // val shirtSize: String?,
    // val hoodieSize: String?,
    // val pantsSize: String?,
    // val foodRestrictions: String?,
    // val epipen: String?, // TODO : Check if bool or str
    // val medicalConditions: String?
)

@Serializable
data class UserAttendance(
    // val id: Int?,
    val type: String?,
    // val from: String?,
    // val to: String?,
    // val markedAt: String?,
    // val user: User?
)
