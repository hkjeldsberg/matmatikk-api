package no.matmatikk.api

import no.matmatikk.api.recipe.RecipeService
import no.matmatikk.api.recipe.model.RecipeRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class MatMatikkApplication

fun main(args: Array<String>) {
    runApplication<MatMatikkApplication>(*args)
}

@Component
class LocalCommandLineRunner(
    @Autowired private val recipeService: RecipeService
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (args.contains("PREFILL")) {
            recipeService.saveRecipe(
                RecipeRequest(
                    "Sodd",
                    "Trøndersodd så klart. Tradisjonell norsk suppe med kjøtt og poteter."
                )
            )
            recipeService.saveRecipe(
                RecipeRequest(
                    "Fårikål",
                    "Norges nasjonalrett. Lam og kål kokt til perfeksjon, servert med poteter."
                )
            )
            recipeService.saveRecipe(
                RecipeRequest(
                    "Raspeball",
                    "Potetball tradisjonelt servert med salt kjøtt og kålrotstappe."
                )
            )
            recipeService.saveRecipe(
                RecipeRequest(
                    "Lutefisk",
                    "Tørket fisk behandlet med lut, servert med ertestuing, bacon og poteter."
                )
            )
            recipeService.saveRecipe(
                RecipeRequest(
                    "Kjøttkaker",
                    "Hjemmelagde kjøttkaker i brun saus, servert med kålstuing og kokte poteter."
                )
            )
            recipeService.saveRecipe(
                RecipeRequest(
                    "Kumla",
                    "Nordnorsk versjon av raspeball, ofte servert med røkt sau."
                )
            )
            recipeService.saveRecipe(
                RecipeRequest(
                    "Rømmegrøt",
                    "En tykk grøt laget av rømme, servert med sukker, kanel og smørklatt."
                )
            )
            recipeService.saveRecipe(
                RecipeRequest(
                    "Pinnekjøtt",
                    "Ribber av sau dampet på bjørkepinner, servert med kålrotstappe og poteter."
                )
            )
            recipeService.saveRecipe(
                RecipeRequest(
                    "Smalahove",
                    "Tradisjonell vestnorsk rett, halvparten av et sauens hode, servert med poteter og kålrotstappe."
                )
            )
            recipeService.saveRecipe(
                RecipeRequest(
                    "Bacalao",
                    "Bacalao fra Lofoten. Torsk lagret i salt, kokt med tomat, løk og poteter."
                )
            )

        }
    }
}
