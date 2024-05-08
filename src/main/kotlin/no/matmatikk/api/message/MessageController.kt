package no.matmatikk.api.message

import no.matmatikk.api.message.model.MessageRequest
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/message")
class MessageController(
    private val messageProducer: MessageProducer,
) {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/message")
    fun broadcastGroupMessage(@Payload messageRequest: MessageRequest) =
        messageProducer.sendMessage(messageRequest.toMessage())


    @PostMapping("/send")
    suspend fun sendMessage(@RequestBody messageRequest: MessageRequest) {
        messageProducer.sendMessage(messageRequest.toMessage())
    }
}