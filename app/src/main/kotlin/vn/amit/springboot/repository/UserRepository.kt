package vn.amit.springboot.repository

import org.springframework.stereotype.Repository
import vn.amit.common.repository.BaseRepository
import vn.amit.entity.auth.User
import java.util.*

@Repository
interface UserRepository : BaseRepository<User, Int> {
    fun findByUsername(username: String): Optional<User>
}