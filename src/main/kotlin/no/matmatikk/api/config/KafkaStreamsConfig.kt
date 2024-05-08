package no.matmatikk.api.config

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.runBlocking
import no.matmatikk.api.message.model.Message
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Produced
import org.springframework.beans.factory.annotation.Autowired
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

    @Value("\${kafka.topic-count}")
    private lateinit var topicCount: String

    @Autowired
    private lateinit var openAI: OpenAI

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
    fun kStream(streamsBuilder: StreamsBuilder): KStream<String, Message>? {
        val stream = streamsBuilder.stream<String, Message>(topicIn)
        stream
            .peek { k, v -> println("[Stream] Key: $k, Value: ${v.content}") }
            .mapValues { message ->
                runBlocking {
                    val openAIMessage = createChatRequest(message.content)
                    message.copy(content = openAIMessage ?: message.content.uppercase())
                }
            }
            .to(topicOut)

        stream
            .groupByKey()
            .count()
            .toStream()
            .peek { k, v -> println("[Count] Key: $k, Value: $v") }
            .to(topicCount, Produced.with(Serdes.String(), Serdes.Long()))

        return stream
    }

    private suspend fun createChatRequest(message: String): String? {
        val request = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = "Skriv følgende text mye høfligere: $message"
                )
            )
        )
        val response = openAI.chatCompletion(request)
        return response.choices.first().message.content
    }

}