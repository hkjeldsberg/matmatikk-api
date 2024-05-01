package no.matmatikk.api.message

import no.matmatikk.api.message.model.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class MessageConsumer {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var template: SimpMessagingTemplate

    @KafkaListener(
        topics = ["\${kafka.topic}"],
        groupId = "groupId"
    )
    fun consume(message: Message) {
        logger.info("Message received $message")
        template.convertAndSend("/topic/group", message)
    }
}