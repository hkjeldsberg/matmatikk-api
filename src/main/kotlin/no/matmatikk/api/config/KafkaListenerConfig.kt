package no.matmatikk.api.config

import no.matmatikk.api.exceptions.KafkaErrorHandler
import no.matmatikk.api.message.model.Message
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.LongDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
@EnableKafka
class KafkaListenerConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Bean
    fun messageConsumerFactory(): ConsumerFactory<String, Message> = DefaultKafkaConsumerFactory(
        mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to "no.matmatikk.api.message.model",
            ConsumerConfig.GROUP_ID_CONFIG to "chatGroupId",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "latest"
        )
    )

    @Bean
    fun messageKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Message> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Message>()
        factory.consumerFactory = messageConsumerFactory()
        factory.setCommonErrorHandler(KafkaErrorHandler())

        return factory
    }

    @Bean
    fun longConsumerFactory(): ConsumerFactory<String, Long> = DefaultKafkaConsumerFactory(
        mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to LongDeserializer::class.java,
            ConsumerConfig.GROUP_ID_CONFIG to "chatGroupId",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "latest"
        )
    )

    @Bean
    fun longKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Long> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Long>()
        factory.consumerFactory = longConsumerFactory()
        factory.setCommonErrorHandler(KafkaErrorHandler())

        return factory
    }
}