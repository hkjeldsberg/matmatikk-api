package no.matmatikk.api.user

import jdk.jfr.Registered
import no.matmatikk.api.auth.LoginRequest
import no.matmatikk.api.exceptions.UserNotFoundException
import no.matmatikk.api.user.model.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    internal fun getCurrentUser(): User? {
        val context = SecurityContextHolder.getContext()
        println(context)
        val id = context.authentication?.name ?: throw UserNotFoundException()
        return userRepository.findByIdOrNull(id) ?: throw UserNotFoundException()
    }

}