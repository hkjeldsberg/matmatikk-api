package no.matmatikk.api

import no.matmatikk.api.message.model.Message
import no.matmatikk.api.recipe.model.RecipeDescriptionRequest
import no.matmatikk.api.recipe.model.RecipeRequest
import no.matmatikk.api.user.model.UserRequest
import java.util.*

internal fun getMockUserRequest(email: String? = null): UserRequest =
    UserRequest(
        firstName = "firstName",
        lastName = "lastName",
        email = email ?: "valid@email.com",
        password = "password"
    )

internal fun getMockMessage(userId: String? = null): Message =
    Message(
        sender = userId ?: UUID.randomUUID().toString(),
        content = "content",
    )

internal fun getMockRecipeRequest(description: String? = null): RecipeRequest = RecipeRequest(
    name = "name",
    description = description ?: "description"
)

internal fun getMockRecipeDescriptionRequest(description: String? = null): RecipeDescriptionRequest =
    RecipeDescriptionRequest(
        description = description ?: "description"
    )