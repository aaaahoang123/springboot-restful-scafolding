package vn.amit.auth.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vn.amit.auth.repository.AccountRepository
import vn.amit.common.rest.RestResponse

@RestController
@RequestMapping("accounts")
class AuthenticationController @Autowired constructor(
        private val accountRepository: AccountRepository
) {

    @PostMapping
    fun create(): RestResponse {
        return RestResponse(
                message = "auth_failed"
        )
    }
}