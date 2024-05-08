package no.matmatikk.api.message

import no.matmatikk.api.exceptions.CustomKafkaException
import no.matmatikk.api.message.model.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class MessageProducer(
    @Value("\${kafka.topic-in}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, Message>,
) {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    fun sendMessage(message: Message) {
        kafkaTemplate.send(topic, message.sender, message).whenComplete { record, ex ->

            if (ex != null) {
                throw CustomKafkaException(ex.message)
            } else {
                log.info(
                    "Published message: key=${record.producerRecord.key()}" +
                            ", value=${record.producerRecord.value()}" +
                            ", topic=${record.recordMetadata.topic()}" +
                            ", partition=${record.recordMetadata.partition()}" +
                            ", offset=${record.recordMetadata.offset()}"
                )
            }
        }
    }
}