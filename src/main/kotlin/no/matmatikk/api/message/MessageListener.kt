package no.matmatikk.api.message

import no.matmatikk.api.message.model.Message
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
        groupId = "chatGroupId"
    )
    fun consume(message: Message) {
        log.info("Message received ${message.content}")
        template.convertAndSend("/topic/message", message)

    }
}