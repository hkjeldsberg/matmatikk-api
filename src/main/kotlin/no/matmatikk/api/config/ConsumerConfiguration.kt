package no.matmatikk.api.config

import no.matmatikk.api.message.model.Message
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@EnableKafka
@Configuration
class ConsumerConfiguration(
    @Value("\${kafka.bootstrap-server}")
    private val bootstrapServer: String
) {

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Message> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Message>()
        factory.consumerFactory = consumerFactory()
        return factory
    }

    @Bean
    fun consumerFactory() = DefaultKafkaConsumerFactory<String, Message>(consumerConfigurations())

    @Bean
    fun consumerConfigurations() = mapOf<String, String>(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.canonicalName,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java.canonicalName,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "latest",
        ConsumerConfig.GROUP_ID_CONFIG to "groupId",
        JsonDeserializer.TRUSTED_PACKAGES to "no.matmatikk.api.message.model"
    )
}