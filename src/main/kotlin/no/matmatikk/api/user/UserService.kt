package no.matmatikk.api.user

import no.matmatikk.api.exceptions.InvalidEmailFormatException
import no.matmatikk.api.exceptions.UserExistException
import no.matmatikk.api.exceptions.UserNotFoundException
import no.matmatikk.api.message.MessageRepository
import no.matmatikk.api.message.model.Message
import no.matmatikk.api.user.model.Role
import no.matmatikk.api.user.model.User
import no.matmatikk.api.user.model.UserRequest
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
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

    internal fun getUser(userId: String) = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException(userId)


    internal fun registerUser(userRequest: UserRequest): User {
        if (!EmailValidator.validateEmail(userRequest.email)) {
            throw InvalidEmailFormatException(userRequest.email)
        }

        if (userRepository.findByEmail(userRequest.email) != null) {
            throw UserExistException(userRequest.email)
        }

        val newUser = User(
            firstName = userRequest.firstName,
            lastName = userRequest.lastName,
            password = passwordEncoder.encode(userRequest.password),
            email = userRequest.email,
            role = Role.USER
        )

        userRepository.save(newUser)
        logger.info("User registered with id=${newUser.id} and assigned role=${newUser.role}")

        return newUser
    }

    fun getMessages(userId: String): List<Message> = messageRepository.findAllBySender(userId)
}