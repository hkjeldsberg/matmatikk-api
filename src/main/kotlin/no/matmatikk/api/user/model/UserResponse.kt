package no.matmatikk.api.user.model


data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String
)