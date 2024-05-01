package no.matmatikk.api.config

import no.matmatikk.api.message.model.Message
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer

@EnableKafka
@Configuration
class ProducerConfiguration(
    @Value("\${kafka.bootstrap-server}")
    private val bootstrapServer: String
) {

    @Bean
    fun producerConfigurations() = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.canonicalName,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java.canonicalName
    )

    @Bean
    fun producerFactory() = DefaultKafkaProducerFactory<String, Message>(producerConfigurations())

    @Bean
    fun kafkaTemplate() = KafkaTemplate(producerFactory())
}