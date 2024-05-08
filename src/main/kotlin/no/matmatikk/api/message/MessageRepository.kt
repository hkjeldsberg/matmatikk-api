package no.matmatikk.api.message

import no.matmatikk.api.message.model.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, String> {
    fun findAllBySender(userId: String): List<Message>
}