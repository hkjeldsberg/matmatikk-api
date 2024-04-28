package no.matmatikk.api.recipe.model

import jakarta.persistence.*

@Entity
@Table(name = "RECIPES")
class Recipe(
    private val name: String,
    private var description: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    internal val id: String = ""

    internal fun toRecipeResponse() =
        RecipeResponse(id = id, name = name, description = description)

    fun updateRecipeDescription(request: RecipeRequest) {
        description = request.description
    }

    companion object {
        internal fun List<Recipe>.toRecipeResponse() = map { it.toRecipeResponse() }
    }
}

