package no.matmatikk.api.exceptions

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.errors.RecordDeserializationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.listener.CommonErrorHandler
import org.springframework.kafka.listener.MessageListenerContainer

class KafkaErrorHandler : CommonErrorHandler {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun handleOne(
        exception: Exception,
        record: ConsumerRecord<*, *>,
        consumer: Consumer<*, *>,
        container: MessageListenerContainer,
    ): Boolean = handle(exception, consumer)


    override fun handleOtherException(
        exception: Exception,
        consumer: Consumer<*, *>,
        container: MessageListenerContainer,
        batchListener: Boolean,
    ) {
        handle(exception, consumer);
    }

    private fun handle(exception: Exception, consumer: Consumer<*, *>): Boolean {
        log.warn("Kafka error handler, error: ${exception.javaClass}, msg: ${exception.message}, record: $consumer")
        if (exception is RecordDeserializationException) {
            consumer.seek(exception.topicPartition(), exception.offset() + 1L);
            consumer.commitSync()
        }
        return true
    }

}