package no.matmatikk.api.recipe

import no.matmatikk.api.recipe.model.Recipe.Companion.toResponse
import no.matmatikk.api.recipe.model.RecipeRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/recipe")
class RecipeController(private val service: RecipeService) {

    @GetMapping
    fun getRecipes() = service.getRecipes().toResponse()

    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable id: String) = service.getRecipe(id).toResponse()

    @PostMapping
    fun saveRecipe(@RequestBody recipeRequest: RecipeRequest) = service.saveRecipe(recipeRequest).toResponse()

    @DeleteMapping("/{id}")
    fun deleteRecipe(@PathVariable id: String) =
        service.deleteRecipe(id)

    @PutMapping("/{id}")
    fun updateRecipe(@PathVariable id: String, @RequestBody recipeRequest: RecipeRequest) =
        service.updateRecipe(id, recipeRequest)
}