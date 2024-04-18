package no.matmatikk.api.recipe.model

import jakarta.persistence.*

@Entity
@Table(name = "RECIPES")
class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,
    private val name: String,
    private val description: String,

    ) {
    internal fun toRecipeResponse() =
        RecipeResponse(id = id, name = name, description = description)
}

