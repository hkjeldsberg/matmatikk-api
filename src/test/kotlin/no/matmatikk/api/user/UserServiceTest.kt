package no.matmatikk.api.user

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.matmatikk.api.exceptions.InvalidEmailFormatException
import no.matmatikk.api.exceptions.UserExistException
import no.matmatikk.api.exceptions.UserNotFoundException
import no.matmatikk.api.user.model.UserRequest
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest(
    @Autowired
    private val userService: UserService,
) {
    @BeforeEach
    fun setUp(@Autowired flyway: Flyway) {
        flyway.clean()
        flyway.migrate()
    }

    @Test
    fun `Register user should throw InvalidEmailException when email has invalid format`() {
        val mockUserRequest = UserRequest(
            firstName = "firstName",
            lastName = "lastName",
            password = "password",
            email = "invalid.email.com"
        )

        shouldThrow<InvalidEmailFormatException> { userService.registerUser(mockUserRequest) }
    }

    @Test
    fun `Register user should throw UserExistException when user already exists`() {
        val mockUserRequest = UserRequest(
            firstName = "firstName",
            lastName = "lastName",
            password = "password",
            email = "valid@email.com"
        )
        userService.registerUser(mockUserRequest)

        shouldThrow<UserExistException> { userService.registerUser(mockUserRequest) }
    }

    @Test
    fun `Register new user`() {
        val mockUserRequest = UserRequest(
            firstName = "firstName",
            lastName = "lastName",
            password = "password",
            email = "valid@email.com"
        )

        val registeredUser = userService.registerUser(mockUserRequest)
        shouldNotThrow<UserNotFoundException> { userService.getUser(registeredUser.id) }
        userService.getUser(registeredUser.id).id shouldBe registeredUser.id
    }
}