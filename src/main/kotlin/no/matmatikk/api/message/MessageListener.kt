package no.matmatikk.api.message

import no.matmatikk.api.message.model.Message
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class MessageListener {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var template: SimpMessagingTemplate


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