package no.matmatikk.api.recipe.model

import io.kotest.matchers.shouldBe
import no.matmatikk.api.getMockRecipeRequest
import org.junit.jupiter.api.Test

class RecipeTest {

    @Test
    fun `Update description for recipe`() {
        val recipe = getMockRecipeRequest().toRecipe()
        val recipeDescriptionRequest = RecipeDescriptionRequest("newDescription")
        recipe.updateRecipeDescription(recipeDescriptionRequest.description)

        recipe.toRecipeResponse().description shouldBe recipeDescriptionRequest.description
    }
}