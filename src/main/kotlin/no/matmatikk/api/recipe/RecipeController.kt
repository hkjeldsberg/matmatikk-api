package no.matmatikk.api.recipe

import no.matmatikk.api.recipe.model.Recipe.Companion.toResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recipe")
class RecipeController(private val service: RecipeService) {

    @GetMapping
    fun getRecipes() = service.getRecipes().toResponse()

    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable id: String) = service.getRecipe(id).toResponse()
}