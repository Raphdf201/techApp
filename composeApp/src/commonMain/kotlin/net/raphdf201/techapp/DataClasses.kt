package net.raphdf201.techapp

import kotlinx.serialization.Serializable

const val absent = "absent"
const val present = "present"
const val techApiHost = "api.team3990.com"

/**
 * Data class representing an event
 */
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
    // val creator: Creator?,
    val attendance: List<Attendance>?,
    val invitations: List<Invitation>?,
    val full: Boolean?,
    val userAttendance: UserAttendance?
)

/**
 * Data class representing the creator of an [Event]
 */
/*
@Serializable
data class Creator(val id: Int?, val completeName: String?)
*/

/**
 * Data class representing the attendance of an [Event]
 */
@Serializable
data class Attendance(
    // val id: Int?,
    val type: String?,
    val from: String?,
    val to: String?,
    // val markedAt: String?,
    val user: User?
)

/**
 * Data class representing the invitation to an [Event]
 */
@Serializable
data class Invitation(val id: Int, val user: User)

/**
 * Data class representing a user
 */
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

/**
 * Data class representing the attendance of a [User] to an [Event]
 */
@Serializable
data class UserAttendance(
    // val id: Int?,
    val type: String?,
    // val from: String?,
    // val to: String?,
    // val markedAt: String?,
    // val user: User?
)
