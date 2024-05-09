package no.matmatikk.api.utils

import java.util.regex.Pattern

class EmailValidator {

    companion object {
        private const val EMAIL_REGEX ="^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"

        private val pattern: Pattern = Pattern.compile(EMAIL_REGEX)

        fun validateEmail(email: String): Boolean {
            return pattern.matcher(email).matches()
        }
    }
}

