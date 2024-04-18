package no.matmatikk.api.recipe

import no.matmatikk.api.recipe.model.Recipe
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeRepository : JpaRepository<Recipe, String> {
}