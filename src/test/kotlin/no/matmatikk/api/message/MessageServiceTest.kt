package no.matmatikk.api.message

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.matmatikk.api.exceptions.MessageNotFoundException
import no.matmatikk.api.getMockMessage
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
class MessageServiceTest(
    @Autowired
    private val messageService: MessageService,
) {
    @BeforeEach
    fun setUp(@Autowired flyway: Flyway) {
        flyway.clean()
        flyway.migrate()
    }

    @Test
    fun `Save message should save message`() {
        val mockMessage = getMockMessage()
        messageService.saveMessage(mockMessage)

        shouldNotThrow<MessageNotFoundException> { messageService.getMessage(mockMessage.id) }
        messageService.getMessage(mockMessage.id).id shouldBe mockMessage.id
    }

    @Test
    fun `Get message should find existing message`() {
        val mockMessage = getMockMessage()
        messageService.saveMessage(mockMessage)

        shouldNotThrow<MessageNotFoundException> { messageService.getMessage(mockMessage.id) }
        messageService.getMessage(mockMessage.id).id shouldBe mockMessage.id
    }

    @Test
    fun `Get message should throw MessageNotFoundException for non-existing message`() {
        val fakeMessageId = UUID.randomUUID().toString()

        shouldThrow<MessageNotFoundException> { messageService.getMessage(fakeMessageId) }
    }

    @Test
    fun `Send message should store message`() {
        val mockMessage = getMockMessage()
        messageService.sendMessage(mockMessage)

        shouldNotThrow<MessageNotFoundException> { messageService.getMessage(mockMessage.id) }
        messageService.getMessage(mockMessage.id).id shouldBe mockMessage.id
    }
}