package no.matmatikk.api.user

import no.matmatikk.api.message.model.Message.Companion.toMessageResponse
import no.matmatikk.api.message.model.MessageResponse
import no.matmatikk.api.user.model.UserRequest
import no.matmatikk.api.user.model.UserResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/current")
    fun getCurrentUser(): UserResponse =
        userService.getCurrentUser().toUserResponse()

    @PostMapping("/register")
    fun registerUser(@RequestBody request: UserRequest): UserResponse =
        userService.registerUser(request).toUserResponse()

    @GetMapping("/{userId}/messages")
    fun getMessages(@PathVariable userId: String): List<MessageResponse> =
        userService.getMessages(userId).toMessageResponse()
}
