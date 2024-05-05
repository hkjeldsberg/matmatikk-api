package no.matmatikk.api.config

import no.matmatikk.api.exceptions.KafkaErrorHandler
import no.matmatikk.api.message.model.Message
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.KafkaStreams
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
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
@EnableKafka
@EnableKafkaStreams
class KafkaConfig(
    @Value("\${kafka.bootstrap-server}")
    private val bootstrapServer: String,
    @Value("\${kafka.topic}")
    private val topic: String,
) {

    @Bean
    fun producerConfigurations() = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
    )

    @Bean
    fun producerFactory() = DefaultKafkaProducerFactory<String, Message>(producerConfigurations())

    @Bean
    fun kafkaTemplate() = KafkaTemplate(producerFactory())

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Message> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Message>()
        factory.consumerFactory = consumerFactory()
        factory.setCommonErrorHandler(KafkaErrorHandler())
        return factory
    }

    @Bean
    fun consumerFactory() = DefaultKafkaConsumerFactory(
        consumerConfigurations(),
        StringDeserializer(),
        JsonDeserializer(Message::class.java)
    )

    @Bean
    fun consumerConfigurations() = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
        ConsumerConfig.GROUP_ID_CONFIG to "groupId"
    )


    @Bean(name = [DEFAULT_STREAMS_CONFIG_BEAN_NAME])
    fun kafkaStreamsConfig(): KafkaStreamsConfiguration = KafkaStreamsConfiguration(
        mapOf(
            StreamsConfig.APPLICATION_ID_CONFIG to "kafka-chat-streams",
            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
            ConsumerConfig.GROUP_ID_CONFIG to "groupId"
        )
    )

    fun messageSerde(): Serde<Message> {
        val serializer = JsonSerializer<Message>()
        val deserializer = JsonDeserializer(Message::class.java)
        deserializer.configure(mapOf(JsonDeserializer.TRUSTED_PACKAGES to "*"), false)
        return Serdes.serdeFrom(serializer, deserializer)
    }

    @Bean
    fun configurer(): StreamsBuilderFactoryBeanConfigurer? {
        return StreamsBuilderFactoryBeanConfigurer { fb: StreamsBuilderFactoryBean ->
            fb.setStateListener { newState: KafkaStreams.State, oldState: KafkaStreams.State ->
                println("State transition from $oldState to $newState")
            }
        }
    }

    @Bean
    fun kStream(streamsBuilder: StreamsBuilder): KStream<String, String> {
        val stream = streamsBuilder.stream(topic, Consumed.with(Serdes.String(), messageSerde()))
        val processedStream = stream.peek { _, value -> println("Received Message: $value") }

        processedStream.to("kafka-chat")
        processedStream.print(Printed.toSysOut())

        return processedStream
    }
}
