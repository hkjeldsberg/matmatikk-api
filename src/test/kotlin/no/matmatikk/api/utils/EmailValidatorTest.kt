package no.matmatikk.api.utils


import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

class EmailValidatorTest {

    @Test
    fun `Should validate invalid emails as false`() {
        val invalidEmails = listOf(
            "example.com",
            "user@example,com"
        )
        invalidEmails.forEach { EmailValidator.validateEmail(it) shouldBe false }
    }

    @Test
    fun `Should validate valid emails as true`() {
        val validEmails = listOf(
            "user@example.com",
            "user.name@example.com",
        )
        validEmails.forEach { EmailValidator.validateEmail(it) shouldBe true }
    }

}

