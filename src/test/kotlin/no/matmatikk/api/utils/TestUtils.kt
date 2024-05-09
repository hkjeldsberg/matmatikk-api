package no.matmatikk.api.utils

import no.matmatikk.api.user.model.UserRequest

internal fun getMockUserRequest(email: String? = null) =
    UserRequest(
        firstName = "firstName",
        lastName = "lastName",
        email = email ?: "valid@email.com",
        password = "password"
    )
