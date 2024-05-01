package no.matmatikk.api.message

import no.matmatikk.api.exceptions.CustomKafkaException
import no.matmatikk.api.message.model.Message
import no.matmatikk.api.message.model.MessageRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutionException

@Service
class MessageProducer(
    @Value("\${kafka.topic}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, Message>,
) {
    val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun sendMessage(messageRequest: MessageRequest) {
        val message = messageRequest.toMessage()
        logger.info("Message sent $message")
        try {
            kafkaTemplate.send(topic, message).get()
        } catch (e: InterruptedException) {
            throw CustomKafkaException(e.message)
        } catch (e: ExecutionException) {
            throw CustomKafkaException(e.message)
        }
    }
}