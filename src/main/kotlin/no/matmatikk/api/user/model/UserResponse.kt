package no.matmatikk.api.user.model

import java.util.UUID


data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String
)
