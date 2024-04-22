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

    internal fun toResponse() =
        RecipeResponse(id = id, name = name, description = description)

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    companion object {
        internal fun List<Recipe>.toResponse() = map { it.toResponse() }
    }
}

