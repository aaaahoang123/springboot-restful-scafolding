package vn.amit.springboot.service

import org.springframework.security.core.userdetails.UserDetailsService
import vn.amit.entity.auth.User
import vn.amit.springboot.request.LoginRequest
import vn.amit.springboot.request.RegisterRequest

interface UserService : UserDetailsService {
    fun register(req: RegisterRequest): User

    fun login(req: LoginRequest): User
}