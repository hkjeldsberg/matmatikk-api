package no.matmatikk.api.exceptions

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND


open class CustomRuntimeException(
    override val message: String,
    val name: String,
    val status: HttpStatus
) : RuntimeException(message)

fun CustomRuntimeException.toExceptionResponseMap(): Map<String, Any> {
    val logger = LoggerFactory.getLogger(this::class.java)
    logger.error("Error: $name – $message")
    return mapOf(
        "error" to name,
        "message" to message,
        "status" to status.value()
    )
}

class RecipeNotFoundException(id: String) : CustomRuntimeException(
    message = "Recipe with id=${id} not found",
    name = "RecipeNotFoundException",
    status = NOT_FOUND
)

class UserNotFoundException() : CustomRuntimeException(
    message = "User not found during login",
    name = "UserNotFoundException",
    status = NOT_FOUND
)



