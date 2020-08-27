package vn.amit.springboot.service.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import vn.amit.common.utils.assignObject
import vn.amit.entity.auth.User
import vn.amit.springboot.repository.UserRepository
import vn.amit.springboot.request.LoginRequest
import vn.amit.springboot.request.RegisterRequest

@Service
class AuthServiceImpl @Autowired constructor(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val authenticationManager: AuthenticationManager
) : AuthService {
    override fun register(req: RegisterRequest): User {
        val user = assignObject(User(), req, listOf("password"))
        user.password = passwordEncoder.encode(req.password)
        return userRepository.save(user)
    }

    override fun login(req: LoginRequest): User {
        val authResult = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(req.username, req.password))
        return authResult.principal as User
    }
}