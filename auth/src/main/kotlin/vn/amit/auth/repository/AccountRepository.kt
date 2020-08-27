package vn.amit.auth.repository

import org.springframework.stereotype.Repository
import vn.amit.common.repository.BaseRepository
import vn.amit.entity.auth.User

@Repository
interface AccountRepository : BaseRepository<User, Int>