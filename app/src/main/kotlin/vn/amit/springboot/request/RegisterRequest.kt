package vn.amit.springboot.request

import org.hibernate.validator.constraints.Length
import vn.amit.springboot.repository.UserRepository
import vn.amit.common.constraint.Unique
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

class RegisterRequest {
    @field:NotEmpty
    @field:Length(max = 255)
    @field:Unique(UserRepository::class)
    var username: String? = null

    @field:NotEmpty
    @field:Length(max = 255)
    @field:Email
    @field:Unique(UserRepository::class)
    var email: String? = null

    @field:NotEmpty
    @field:Length(max = 50)
    var password: String? = null
}