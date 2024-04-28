package no.matmatikk.api.user.model

import jakarta.persistence.*

@Entity
@Table(name = "USERS")
class User(
    private val firstName: String,
    private val lastName: String,
    val email: String,
    val password: String,
    @Enumerated(EnumType.STRING)
    val role: Role
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    internal val id = "";

    fun toUserResponse() =
        UserResponse(id = id, firstName = firstName, lastName = lastName, email = email)
}

enum class Role {
    USER, ADMIN
}