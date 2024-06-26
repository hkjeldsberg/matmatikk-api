package no.matmatikk.api.exceptions

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*


open class CustomRuntimeException(
    override val message: String,
    val name: String,
    val status: HttpStatus,
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

class UserNotFoundException(message: String) : CustomRuntimeException(
    message = message,
    name = "UserNotFoundException",
    status = NOT_FOUND
)

class UserNotFoundByEmailException(email: String) : CustomRuntimeException(
    message = "User with email=$email not found",
    name = "UserNotFoundException",
    status = NOT_FOUND
)


class UsernameNotFoundException(email: String) : CustomRuntimeException(
    message = "User with email=$email not found",
    name = "UsernameNotFoundException",
    status = NOT_FOUND
)

class UserWithEmailExistException(email: String) : CustomRuntimeException(
    message = "User with email=$email already exist",
    name = "UserWithEmailExistException",
    status = CONFLICT
)

class UserExistException(id: String) : CustomRuntimeException(
    message = "User with id=$id already exist",
    name = "UserExistException",
    status = CONFLICT
)

class InvalidEmailFormatException(email: String) : CustomRuntimeException(
    message = "Invalid email format: $email",
    name = "EmailFormatException",
    status = BAD_REQUEST
)

class CustomKafkaException(message: String?) : CustomRuntimeException(
    message = message ?: "Kafka producer/consumer threw an exception",
    name = "CustomKafkaException",
    status = BAD_REQUEST
)

class MessageNotFoundException(id: String) : CustomRuntimeException(
    message = "Message with id=$id not found",
    name = "MessageNotFoundException",
    status = NOT_FOUND
)