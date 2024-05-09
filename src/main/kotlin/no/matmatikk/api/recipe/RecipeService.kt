package no.matmatikk.api.recipe

import jakarta.transaction.Transactional
import no.matmatikk.api.exceptions.RecipeNotFoundException
import no.matmatikk.api.recipe.model.Recipe
import no.matmatikk.api.recipe.model.RecipeDescriptionRequest
import no.matmatikk.api.recipe.model.RecipeRequest
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    internal fun saveRecipe(request: RecipeRequest): Recipe {
        val recipe = recipeRepository.save(request.toRecipe())

        logger.info("Recipe stored: ${request.name}")
        return recipe
    }
    internal fun getRecipe(id: String) = recipeRepository.findByIdOrNull(id) ?: throw RecipeNotFoundException(id)

    internal fun getRecipes() = recipeRepository.findAll()


    internal fun deleteRecipe(id: String) {
        recipeRepository.delete(getRecipe(id))
    }

    @Transactional
    internal fun updateRecipe(recipeId: String, request: RecipeDescriptionRequest) {
        getRecipe(recipeId).updateRecipeDescription(request.description)
    }
}