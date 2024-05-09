package no.matmatikk.api.user

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.matmatikk.api.exceptions.InvalidEmailFormatException
import no.matmatikk.api.exceptions.UserNotFoundByEmailException
import no.matmatikk.api.exceptions.UserNotFoundException
import no.matmatikk.api.exceptions.UserWithEmailExistException
import no.matmatikk.api.message.MessageService
import no.matmatikk.api.getMockMessage
import no.matmatikk.api.getMockUserRequest
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import java.security.Principal
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val messageService: MessageService,
) {
    @BeforeEach
    fun setUp(@Autowired flyway: Flyway) {
        flyway.clean()
        flyway.migrate()
    }

    @Test
    fun `Get current user should return current user`() {
        val user = userService.registerUser(getMockUserRequest())
        val email = user.toUserResponse().email
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(MockPrincipal(email), null)

        user.toUserResponse() shouldBe userService.getCurrentUser().toUserResponse()

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `Get current user should throw UserNotFoundException if no user is found on SecurityContext`() {
        shouldThrow<UserNotFoundException> { userService.getCurrentUser().toUserResponse() }
    }

    @Test
    fun `Get current user should throw UserNotFoundByEmailException if no user is found by email`() {
        val email = "email.without.user@com"
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(MockPrincipal(email), null)

        shouldThrow<UserNotFoundByEmailException> { userService.getCurrentUser().toUserResponse() }

    }

    @Test
    fun `Get user should throw UserNotFoundException if user not found`() {
        val fakeUserId = UUID.randomUUID().toString()

        shouldThrow<UserNotFoundException> { userService.getUser(fakeUserId) }
    }

    @Test
    fun `Register user should throw InvalidEmailException when email has invalid format`() {
        val mockUserRequestWithInvalidEmail = getMockUserRequest(email = "invalid.email.com")

        shouldThrow<InvalidEmailFormatException> { userService.registerUser(mockUserRequestWithInvalidEmail) }
    }

    @Test
    fun `Register user should throw UserWithEmailExistException when user already exists`() {
        val mockUserRequest = getMockUserRequest()
        userService.registerUser(mockUserRequest)

        shouldThrow<UserWithEmailExistException> { userService.registerUser(mockUserRequest) }
    }

    @Test
    fun `Register new user`() {
        val user = userService.registerUser(getMockUserRequest())

        shouldNotThrow<UserNotFoundException> { userService.getUser(user.id) }
        userService.getUser(user.id).id shouldBe user.id
    }

    @Test
    fun `Get messages should return a list of messages when messages are present`() {
        val user = userService.registerUser(getMockUserRequest())
        val message = getMockMessage(user.id)

        messageService.saveMessage(message)

        val fetchedMessages = userService.getMessages(user.id)

        fetchedMessages.isEmpty() shouldBe false
        fetchedMessages.first().sender shouldBe message.sender
        fetchedMessages.first().sender shouldBe user.id
    }

    @Test
    fun `Get messages should return an empty list of messages when no messages are present`() {
        val user = userService.registerUser(getMockUserRequest())

        userService.getMessages(user.id).isEmpty() shouldBe true
    }
}

class MockPrincipal(private val email: String) : Principal {
    override fun getName() = email
}
