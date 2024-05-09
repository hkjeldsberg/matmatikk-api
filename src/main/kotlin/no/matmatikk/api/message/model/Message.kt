package no.matmatikk.api.message.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "MESSAGES")
data class Message(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val sender: String,
    val content: String,
    private val timestamp: Long = System.currentTimeMillis(),
) {

    fun toMessageResponse() = MessageResponse(
        sender = sender,
        content = content,
        timestamp = timestamp
    )

    companion object {
        internal fun List<Message>.toMessageResponse() = map { it.toMessageResponse() }
    }

}