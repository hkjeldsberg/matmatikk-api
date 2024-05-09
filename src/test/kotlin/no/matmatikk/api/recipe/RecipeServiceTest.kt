package no.matmatikk.api.recipe

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.matmatikk.api.exceptions.RecipeNotFoundException
import no.matmatikk.api.getMockRecipeDescriptionRequest
import no.matmatikk.api.getMockRecipeRequest
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*


@SpringBootTest
@ActiveProfiles("test")
class RecipeServiceTest(
    @Autowired
    private val recipeService: RecipeService,
) {
    @BeforeEach
    fun setUp(@Autowired flyway: Flyway) {
        flyway.clean()
        flyway.migrate()
    }

    @Test
    fun `Recipe should be saved`() {
        val recipe = recipeService.saveRecipe(getMockRecipeRequest())

        shouldNotThrow<RecipeNotFoundException> { recipeService.getRecipe(recipe.id) }
        recipeService.getRecipe(recipe.id).id shouldBe recipe.id
    }

    @Test
    fun `Get recipe should throw RecipeNotFoundException if recipe does not exist`() {
        val fakeRecipeId = UUID.randomUUID().toString()

        shouldThrow<RecipeNotFoundException> { recipeService.getRecipe(fakeRecipeId) }
    }

    @Test
    fun `Get recipe should return recipe if it exists`() {
        val recipe = recipeService.saveRecipe(getMockRecipeRequest())

        shouldNotThrow<RecipeNotFoundException> { recipeService.getRecipe(recipe.id) }
        recipeService.getRecipe(recipe.id).id shouldBe recipe.id
    }

    @Test
    fun `Recipe should be deleted`() {
        val recipe = recipeService.saveRecipe(getMockRecipeRequest())

        recipeService.getRecipe(recipe.id).id shouldBe recipe.id
        recipeService.deleteRecipe(recipe.id)
        shouldThrow<RecipeNotFoundException> { recipeService.getRecipe(recipe.id) }
    }

    @Test
    fun `Delete recipe should throw RecipeNotFoundException if recipe does not exist`() {
        val fakeRecipeId = UUID.randomUUID().toString()

        shouldThrow<RecipeNotFoundException> { recipeService.deleteRecipe(fakeRecipeId) }
    }

    @Test
    fun `Update recipe should update recipe description`() {
        val recipe = recipeService.saveRecipe(getMockRecipeRequest())
        val recipeDescriptionRequest= getMockRecipeDescriptionRequest("newDescription")
        recipeService.getRecipe(recipe.id).toRecipeResponse().description shouldBe recipe.toRecipeResponse().description

        recipeService.updateRecipe(recipe.id, recipeDescriptionRequest)
        recipeService.getRecipe(recipe.id).toRecipeResponse().description shouldBe recipeDescriptionRequest.description
    }

}
