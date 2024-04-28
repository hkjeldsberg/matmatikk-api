package no.matmatikk.api.recipe

import no.matmatikk.api.recipe.model.Recipe.Companion.toRecipeResponse
import no.matmatikk.api.recipe.model.RecipeRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/recipe")
class RecipeController(private val service: RecipeService) {

    @GetMapping
    fun getRecipes() = service.getRecipes().toRecipeResponse()

    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable id: String) = service.getRecipe(id).toRecipeResponse()

    @PostMapping
    fun saveRecipe(@RequestBody recipeRequest: RecipeRequest) = service.saveRecipe(recipeRequest).toRecipeResponse()

    @DeleteMapping("/{id}")
    fun deleteRecipe(@PathVariable id: String) {
        service.deleteRecipe(id)
    }

    @PutMapping("/{id}")
    fun updateRecipe(@PathVariable id: String, @RequestBody recipeRequest: RecipeRequest) {
        service.updateRecipe(id, recipeRequest)
    }
}