package no.matmatikk.api.user

import java.util.regex.Pattern

class EmailValidator {

    companion object {
        private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"

        private val pattern: Pattern = Pattern.compile(EMAIL_REGEX)

        fun validateEmail(email: String): Boolean {
            return pattern.matcher(email).matches()
        }
    }
}

