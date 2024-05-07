package no.matmatikk.api.message

import no.matmatikk.api.exceptions.CustomKafkaException
import no.matmatikk.api.message.model.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutionException

@Service
class MessageProducer(
    @Value("\${kafka.topic-in}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, Message>,
) {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    fun sendMessage(message: Message) {
        log.info("Sending message ${message.content}")
        try {
            kafkaTemplate.send(topic, message)
        } catch (e: InterruptedException) {
            throw CustomKafkaException(e.message)
        } catch (e: ExecutionException) {
            throw CustomKafkaException(e.message)
        }
    }
}