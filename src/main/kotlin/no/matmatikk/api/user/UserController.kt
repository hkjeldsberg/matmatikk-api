package no.matmatikk.api.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    fun getCurrentUser() = userService.getCurrentUser()?.toUserResponse()
}
