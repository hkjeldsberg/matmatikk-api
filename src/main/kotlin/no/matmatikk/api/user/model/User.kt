package no.matmatikk.api.user.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "USERS")
data class User(
    @Id
    val id: String = UUID.randomUUID().toString(),
    private val firstName: String,
    private val lastName: String,
    val email: String,
    val password: String,
    @Enumerated(EnumType.STRING)
    val role: Role,
) {

    fun toUserResponse() =
        UserResponse(id = id, firstName = firstName, lastName = lastName, email = email)
}

enum class Role {
    USER, ADMIN
}