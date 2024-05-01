package no.matmatikk.api.message.model

data class Message(
    val sender: String,
    val content: String,
    val timestamp: String
) {
    override fun toString() =
        "Message(sender='$sender', content='$content', timestamp='$timestamp')"
}