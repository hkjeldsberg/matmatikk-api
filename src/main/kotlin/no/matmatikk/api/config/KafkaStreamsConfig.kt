package no.matmatikk.api.config

import no.matmatikk.api.message.model.Message
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Printed
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

@EnableKafka
@EnableKafkaStreams
@Configuration
class KafkaStreamsConfig(
    @Value("\${kafka.bootstrap-server}")
    private val bootstrapServer: String,
    @Value("\${kafka.topic}")
    private val topic: String,
) {


//    @Bean(name = [DEFAULT_STREAMS_CONFIG_BEAN_NAME])
//    fun kStreamsConfig(): KafkaStreamsConfiguration = KafkaStreamsConfiguration(
//        mapOf(
//            StreamsConfig.APPLICATION_ID_CONFIG to "kafka-chat-streams",
//            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
//            StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG to "0", // No caching
//            StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG to Serdes.String().javaClass.name,
//            StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG to messageSerde().javaClass.name
//        )
//    )

    fun messageSerde(): Serde<Message> {
        val serializer = JsonSerializer<Message>()
        val deserializer = JsonDeserializer(Message::class.java)
        deserializer.configure(mapOf(JsonDeserializer.TRUSTED_PACKAGES to "*"), false)
        return Serdes.serdeFrom(serializer, deserializer)
    }

    @Bean
    fun kStream(streamsBuilder: StreamsBuilder): KStream<String, Message> {
        val stream =
            streamsBuilder.stream<String, Message>(topic, Consumed.with(Serdes.String(), messageSerde()))
        stream.peek { key, value -> println("Message: $value") }.mapValues { values -> values }
            .to("kafka-chat-caps")
        stream.print(Printed.toSysOut())

        return stream
    }
}
