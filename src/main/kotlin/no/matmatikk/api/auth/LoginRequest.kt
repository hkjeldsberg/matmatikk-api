package no.matmatikk.api.auth

data class LoginRequest(
    val email: String,
    val password: String
)
