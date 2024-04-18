package no.matmatikk.api.ingredients.model

import jakarta.persistence.*
import no.matmatikk.api.recipe.model.Recipe

@Entity
@Table(name = "INGREDIENTS")
class Ingredient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    private val name: String,
    private val quantity: Double,
    private val unit: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private val recipe: Recipe
)