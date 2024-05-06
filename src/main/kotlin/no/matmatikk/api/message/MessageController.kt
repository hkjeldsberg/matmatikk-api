package no.matmatikk.api.message

import no.matmatikk.api.exceptions.CustomKafkaException
import no.matmatikk.api.message.model.Message
import no.matmatikk.api.message.model.MessageRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ExecutionException

@RestController
@RequestMapping("/message")
class MessageController(
    @Value("\${kafka.topic-in}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, Message>,
) {
    val log: Logger = LoggerFactory.getLogger(javaClass)


    @PostMapping("/send")
    fun sendMessage(@RequestBody messageRequest: MessageRequest) {
        log.info("Sending message ${messageRequest.content}")
        try {
            kafkaTemplate.send(topic, messageRequest.toMessage())
        } catch (e: InterruptedException) {
            throw CustomKafkaException(e.message)
        } catch (e: ExecutionException) {
            throw CustomKafkaException(e.message)
        }

    }
}