package no.matmatikk.api.auth

import no.matmatikk.api.auth.model.LoginRequest
import no.matmatikk.api.auth.model.LoginResponse
import no.matmatikk.api.config.JwtProperties
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*


@Service
class AuthService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties
) {
    fun login(request: LoginRequest): LoginResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )

        val user = userDetailsService.loadUserByUsername(request.email)
        val accessToken = generateAccessToken(user)

        return LoginResponse(
            accessToken = accessToken,
            success = true,
            message = "Authentication was successful"
        )
    }

    private fun generateAccessToken(user: UserDetails) = tokenService.generate(
        userDetails = user, expirationDate = getAccessTokenExpiration()
    )

    private fun getAccessTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
}

