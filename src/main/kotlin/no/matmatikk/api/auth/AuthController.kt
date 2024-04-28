package no.matmatikk.api.auth

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    @PostMapping("/signup")
    fun signUp(@RequestBody request: SignUpRequest) {
        println(request)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) {
        println(request)
    }
}