package no.matmatikk.api.recipe.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "RECIPES")
data class Recipe(
    @Id
    val id: String = UUID.randomUUID().toString(),
    private val name: String,
    private var description: String,
) {
    internal fun toRecipeResponse() =
        RecipeResponse(id = id, name = name, description = description)

    fun updateRecipeDescription(updatedDescription: String) {
        description = updatedDescription
    }

    companion object {
        internal fun List<Recipe>.toRecipeResponse() = map { it.toRecipeResponse() }
    }
}

data class RecipeDescriptionRequest(
    val description: String
)


