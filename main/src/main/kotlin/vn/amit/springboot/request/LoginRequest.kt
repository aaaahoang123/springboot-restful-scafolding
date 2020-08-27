package vn.amit.springboot.request

import vn.amit.common.constraint.Exists
import vn.amit.springboot.repository.UserRepository
import javax.validation.constraints.NotEmpty

class LoginRequest {
    @field:NotEmpty
    @field:Exists(UserRepository::class, "username")
    lateinit var username: String

    @field:NotEmpty
    lateinit var password: String
}