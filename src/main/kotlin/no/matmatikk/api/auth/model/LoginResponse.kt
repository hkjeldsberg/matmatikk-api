package no.matmatikk.api.auth.model

data class LoginResponse(
    val accessToken: String,
    val message: String,
    val success: Boolean
)
