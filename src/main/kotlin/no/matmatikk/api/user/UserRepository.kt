package no.matmatikk.api.user

import no.matmatikk.api.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface UserRepository : JpaRepository<User, String> {
    fun findByEmail(email: String): User?
}