package no.matmatikk.api.config

import no.matmatikk.api.message.model.Message
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler
import org.apache.kafka.streams.kstream.KStream
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerde

@Configuration
@EnableKafkaStreams
class KafkaStreamsConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Value("\${kafka.topic-in}")
    private lateinit var topicIn: String

    @Value("\${kafka.topic-out}")
    private lateinit var topicOut: String


    @Bean(name = [KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME])
    fun myKafkaStreamsConfig(): KafkaStreamsConfiguration =
        KafkaStreamsConfiguration(
            mapOf(
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
                StreamsConfig.APPLICATION_ID_CONFIG to "chat-group-id",
                StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG to Serdes.String().javaClass,
                StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG to JsonSerde::class.java,
                StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG to LogAndContinueExceptionHandler::class.java,
                JsonDeserializer.TRUSTED_PACKAGES to "no.matmatikk.api.message.model",
            )
        )

    @Bean
    fun kStream(streamsBuilder: StreamsBuilder): KStream<String, Message> {
        val stream = streamsBuilder.stream<String, Message>(topicIn)

        stream
            .mapValues { message -> message.copy(content = message.content.uppercase()) }
            .to(topicOut)

        return stream
    }
}