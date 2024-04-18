package no.matmatikk.api.recipe

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart

@RequestMapping("/recipe")
class RecipeController(private val recipeService: RecipeService) {

    @GetMapping
    fun getPlants() = recipeService.getRecipes().map { it.toRecipeResponse() }

//    @GetMapping("/{id}")
//    fun getPlantsById(@PathVariable id: String) = recipeService.getRecipe(id).map { it.toRecipeResponse() }


}