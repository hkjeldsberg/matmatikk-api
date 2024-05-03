package no.matmatikk.api.config

import no.matmatikk.api.message.model.Message
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class MessageTopology(
    @Value("\${kafka.topic}") private val topic: String,
) {

    private val log = LoggerFactory.getLogger(MessageTopology::class.java)

    fun processStream(streamsBuilder: StreamsBuilder): KStream<String, Message> {
        return streamsBuilder
            .stream<String?, Message?>(topic)
            .peek { _, v -> log.info("Event: $v") }
    }

}
