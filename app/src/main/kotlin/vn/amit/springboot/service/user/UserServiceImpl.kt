package vn.amit.springboot.service.user

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import vn.amit.security.TokenUserDetailService
import vn.amit.springboot.repository.UserRepository
import vn.amit.token.TokenEncoder

@Service
class UserServiceImpl @Autowired constructor(
        private val tokenEncoder: TokenEncoder,
        private val userRepository: UserRepository
) : UserService, TokenUserDetailService {
    override fun retrieveUsernameByToken(token: String): String {
        return tokenEncoder.decode(token)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username).get()
    }
}