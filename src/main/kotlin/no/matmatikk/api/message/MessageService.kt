package no.matmatikk.api.message

import no.matmatikk.api.exceptions.CustomKafkaException
import no.matmatikk.api.message.model.Message
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class MessageService(
    @Value("\${kafka.topic-in}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, Message>,
    private val messageRepository: MessageRepository,
) {

    @Autowired
    lateinit var template: SimpMessagingTemplate

    val log: Logger = LoggerFactory.getLogger(javaClass)

    fun saveMessage(message: Message) = messageRepository.save(message)

    fun sendMessage(message: Message) {
        // FIXME: Save before or after Kafka processing?
        saveMessage(message)

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

    @KafkaListener(
        topics = ["\${kafka.topic-out}"],
        containerFactory = "messageKafkaListenerContainerFactory"
    )
    fun consumeMessage(messageRecord: ConsumerRecord<String, Message>) {
        log.info("Message received: ${messageRecord.value().content}")
        template.convertAndSend("/topic/message", messageRecord.value())
    }

    @KafkaListener(
        topics = ["\${kafka.topic-count}"],
        containerFactory = "longKafkaListenerContainerFactory"
    )
    fun consumeCount(counterRecord: ConsumerRecord<String, Long>) {
        log.info("Counter received: ${counterRecord.value()}")
    }
}