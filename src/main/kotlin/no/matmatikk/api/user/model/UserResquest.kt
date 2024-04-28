package no.matmatikk.api.user.model

data class UserRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
)
