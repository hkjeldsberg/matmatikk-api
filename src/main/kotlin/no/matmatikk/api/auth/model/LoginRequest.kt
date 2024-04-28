package no.matmatikk.api.auth.model

data class LoginRequest(
    val email: String,
    val password: String
)
