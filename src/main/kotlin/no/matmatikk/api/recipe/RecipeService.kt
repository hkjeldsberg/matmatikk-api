package no.matmatikk.api.recipe

import no.matmatikk.api.exceptions.RecipeNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RecipeService(private val recipeRepository: RecipeRepository) {

    internal fun getRecipes() = recipeRepository.findAll().toList()

    internal fun getRecipe(id: String) = recipeRepository.findByIdOrNull(id) ?: throw RecipeNotFoundException(id)
}