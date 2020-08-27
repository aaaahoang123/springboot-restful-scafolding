package vn.amit.springboot.controller.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import vn.amit.springboot.request.RegisterRequest
import vn.amit.entity.auth.UserDto
import vn.amit.springboot.request.LoginRequest
import vn.amit.springboot.service.UserService
import vn.amit.token.TokenEncoder
import javax.validation.Valid

@RestController
@RequestMapping("/api/v0/auth")
class AuthenticationController @Autowired constructor(
        private val tokenEncoder: TokenEncoder,
        private val userService: UserService
) {

    @PostMapping("/register")
    fun register(@RequestBody @Valid req: RegisterRequest): Map<String, Any> {
        val user = userService.register(req)
        return mapOf(
                "user" to UserDto(user),
                "token" to tokenEncoder.encode(user)
        )
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid req: LoginRequest): Map<String, Any> {
        val user = userService.login(req)
        return mapOf(
                "user" to UserDto(user),
                "token" to tokenEncoder.encode(user)
        )
    }
}