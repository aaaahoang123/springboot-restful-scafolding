package vn.amit.springboot.service.auth

import vn.amit.entity.auth.User
import vn.amit.springboot.request.LoginRequest
import vn.amit.springboot.request.RegisterRequest

interface AuthService {
    fun register(req: RegisterRequest): User
    fun login(req: LoginRequest): User
}