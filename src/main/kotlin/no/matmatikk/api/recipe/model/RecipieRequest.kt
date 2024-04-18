package no.matmatikk.api.recipe.model

data class RecipeRequest(
    val name: String, val description: String
) {
    internal fun toRecipe() = Recipe(
        name = name,
        description = description
    )
}
