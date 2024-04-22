package no.matmatikk.api.recipe

import no.matmatikk.api.exceptions.RecipeNotFoundException
import no.matmatikk.api.recipe.model.Recipe
import no.matmatikk.api.recipe.model.RecipeRequest
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RecipeService(private val repository: RecipeRepository) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    internal fun getRecipes() = repository.findAll()

    internal fun getRecipe(id: String) = repository.findByIdOrNull(id) ?: throw RecipeNotFoundException(id)

    internal fun saveRecipe(request: RecipeRequest): Recipe {
        val recipe = repository.save(request.toRecipe())

        logger.info("Recipe stored: ${request.name}")
        return recipe
    }

    internal fun deleteRecipe(id: String) {
        repository.delete(getRecipe(id))
    }

    internal fun updateRecipe(recipeId: String, request: RecipeRequest) {
        getRecipe(recipeId).updateDescription(request.description)
    }
}