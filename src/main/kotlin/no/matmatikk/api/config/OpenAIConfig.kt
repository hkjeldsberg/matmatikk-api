package no.matmatikk.api.config

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAI
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.time.Duration.Companion.seconds

@Configuration

class OpenAIConfig {

    @Value("\${openai.key}")
    private lateinit var token: String

    @Bean
    fun openAIClient(): OpenAI = OpenAI(
        token = token,
        timeout = Timeout(socket = 60.seconds),
        logging = LoggingConfig(LogLevel.None)
    )

}
