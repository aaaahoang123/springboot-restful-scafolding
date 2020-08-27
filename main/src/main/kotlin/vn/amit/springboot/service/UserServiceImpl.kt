package vn.amit.springboot.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import vn.amit.common.exception.ExecuteException
import vn.amit.common.utils.assignObject
import vn.amit.entity.auth.User
import vn.amit.springboot.repository.UserRepository
import vn.amit.security.TOKEN_USER_DETAIL_SERVICE
import vn.amit.security.TokenUserDetailService
import vn.amit.springboot.request.LoginRequest
import vn.amit.springboot.request.RegisterRequest
import vn.amit.token.TokenEncoder

@Service(TOKEN_USER_DETAIL_SERVICE)
class UserServiceImpl @Autowired constructor(
        private val tokenEncoder: TokenEncoder,
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder
) : TokenUserDetailService(), UserService {
    override fun loadByToken(token: String): UserDetails {
        val userId = tokenEncoder.decode(token).toInt()
        return userRepository.findById(userId).get()
    }

    override fun register(req: RegisterRequest): User {
        val user = assignObject(User(), req, listOf("password"))
        user.password = passwordEncoder.encode(req.password)
        return userRepository.save(user)
    }

    override fun login(req: LoginRequest): User {
        val user = userRepository.findByUsername(req.username).get()

        if (!passwordEncoder.matches(req.password, user.password)) {
            throw ExecuteException("password_not_match", HttpStatus.BAD_REQUEST)
        }

        return user
    }
}