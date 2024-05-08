package no.matmatikk.api.user

import no.matmatikk.api.exceptions.InvalidEmailFormatException
import no.matmatikk.api.exceptions.UserExistException
import no.matmatikk.api.exceptions.UserNotFoundByEmailException
import no.matmatikk.api.exceptions.UserNotFoundException
import no.matmatikk.api.message.MessageRepository
import no.matmatikk.api.message.model.Message
import no.matmatikk.api.user.model.Role
import no.matmatikk.api.user.model.User
import no.matmatikk.api.user.model.UserRequest
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    internal fun getCurrentUser(): User {
        val context = SecurityContextHolder.getContext()
        val id = context.authentication?.name ?: throw UserNotFoundException("Authenticated user ID not found")
        return userRepository.findByEmail(id) ?: throw UserNotFoundException("User not found with ID: $id")
    }

    internal fun registerUser(request: UserRequest): User {
        if (userRepository.findByEmail(request.email) != null) {
            throw UserExistException(request.email)
        }


        if (!EmailValidator.validateEmail(request.email)) {
            throw InvalidEmailFormatException(request.email)
        }

        val newUser = User(
            firstName = request.firstName,
            lastName = request.lastName,
            password = passwordEncoder.encode(request.password),
            email = request.email,
            role = Role.USER
        )

        userRepository.save(newUser)
        logger.info("User registered with email=${newUser.email} and assigned role=${newUser.role}")

        return newUser
    }

    internal fun findByEmail(email: String): User =
        userRepository.findByEmail(email) ?: throw UserNotFoundByEmailException(email)

    fun getMessages(userId: String): List<Message> = messageRepository.findAllBySender(userId)
}