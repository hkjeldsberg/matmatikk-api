package no.matmatikk.api.user

import no.matmatikk.api.user.model.UserRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/current")
    fun getCurrentUser() = userService.getCurrentUser()?.toUserResponse()

    @PostMapping("/register")
    fun registerUser(@RequestBody request: UserRequest) = userService.registerUser(request).toUserResponse()
}
