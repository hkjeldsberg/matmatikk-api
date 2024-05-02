package no.matmatikk.api.message

import no.matmatikk.api.message.model.Message
import no.matmatikk.api.message.model.MessageRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class MessageListener {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var template: SimpMessagingTemplate

    @KafkaListener(
        topics = ["\${kafka.topic}"],
        groupId = "groupId"
    )
    fun consume(message: Message) {
        logger.info("Message received $message")
        val newMessage: Message = MessageRequest(
            content = message.content.uppercase(),
            sender = message.sender
        ).toMessage()

        template.convertAndSend("/topic/message", newMessage)
    }
}