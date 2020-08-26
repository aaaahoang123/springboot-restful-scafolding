package vn.amit.springboot.controller.auth

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vn.amit.entity.auth.User
import vn.amit.entity.auth.UserDto
import vn.amit.security.ReqUser

@RestController
@RequestMapping("/api/v1/user-profile")
class UserProfileController {

    @GetMapping
    fun getUserProfile(
            @ReqUser user: User,
            @RequestHeader(HttpHeaders.AUTHORIZATION) token: String
    ): Map<String, Any> {
        return mapOf(
                "user" to UserDto(user),
                "token" to token
        )
    }
}