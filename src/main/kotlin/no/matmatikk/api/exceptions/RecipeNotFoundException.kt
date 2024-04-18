package no.matmatikk.api.exceptions

import org.hibernate.annotations.NotFound

class RecipeNotFoundException(id: String) : RuntimeException(
)


