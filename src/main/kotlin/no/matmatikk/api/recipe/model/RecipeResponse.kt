package no.matmatikk.api.recipe.model

import no.matmatikk.api.ingredients.model.Ingredient

data class RecipeResponse(
    val id: String,
    val name: String,
    val description: String
)