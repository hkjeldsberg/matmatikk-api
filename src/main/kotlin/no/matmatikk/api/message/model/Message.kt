package no.matmatikk.api.message.model

import jakarta.persistence.*

@Entity
@Table(name = "MESSAGES")
 data class Message(
    private val timestamp: Long = System.currentTimeMillis(),
    val sender: String,
    val content: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    internal val id = "";

    fun toMessageResponse() = MessageResponse(
        sender=sender,
        content=content,
        timestamp=timestamp
    )

    companion object {
        internal fun List<Message>.toMessageResponse() = map { it.toMessageResponse() }
    }

}