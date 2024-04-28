package no.matmatikk.api.user.model

import jakarta.persistence.*

@Entity
@Table(name = "USERS")
class User(
    private val firstName: String,
    private val lastName: String,
    private val email: String,
    private val password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    internal val id = "";

    fun toUserResponse() =
        UserResponse(id = id, firstName = firstName, lastName = lastName, password = password, email = email)
}