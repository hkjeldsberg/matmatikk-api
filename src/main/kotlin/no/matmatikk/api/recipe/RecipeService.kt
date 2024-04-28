package no.matmatikk.api.recipe

import jakarta.transaction.Transactional
import no.matmatikk.api.exceptions.RecipeNotFoundException
import no.matmatikk.api.recipe.model.Recipe
import no.matmatikk.api.recipe.model.RecipeRequest
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RecipeService(
    private val recipieRepository: RecipeRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    internal fun getRecipes() = recipieRepository.findAll()

    internal fun getRecipe(id: String) = recipieRepository.findByIdOrNull(id) ?: throw RecipeNotFoundException(id)

    internal fun saveRecipe(request: RecipeRequest): Recipe {
        val recipe = recipieRepository.save(request.toRecipe())

        logger.info("Recipe stored: ${request.name}")
        return recipe
    }

    internal fun deleteRecipe(id: String) {
        recipieRepository.delete(getRecipe(id))
    }

    @Transactional
    internal fun updateRecipe(recipeId: String, request: RecipeRequest) {
        getRecipe(recipeId).updateRecipeDescription(request)
    }
}