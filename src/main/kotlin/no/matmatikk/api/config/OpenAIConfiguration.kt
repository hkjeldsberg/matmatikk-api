package no.matmatikk.api.config

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.time.Duration.Companion.seconds

@Configuration
class OpenAIConfiguration {

    private var token: String = "myToken"

    @Bean
    fun openAIClient(): OpenAI = OpenAI(
        token = token,
        timeout = Timeout(socket = 60.seconds),
    )

    @Bean
    fun chatCompletionRequest() = ChatCompletionRequest(
        model = ModelId("gpt-3.5-turbo"),
        messages = listOf(
            ChatMessage(
                role = ChatRole.User,
                content = "What is your name?"
            )
        )
    )
}
