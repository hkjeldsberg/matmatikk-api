package no.matmatikk.api.recipe

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/recipe")
class RecipeController(private val service: RecipeService) {

    @GetMapping
    fun getRecipes() = service.getRecipes().map { it.toRecipeResponse() }

    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable id: String) = service.getRecipe(id)
}