package no.matmatikk.api.auth

data class SignUpRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
)
