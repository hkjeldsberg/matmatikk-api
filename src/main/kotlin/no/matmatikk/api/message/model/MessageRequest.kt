package no.matmatikk.api.message.model

import java.time.LocalDateTime

data class MessageRequest(
    val sender: String,
    val content: String,
) {

    internal fun toMessage() = Message(
        sender = sender,
        content = content,
        timestamp = LocalDateTime.now().toString()
    )
}