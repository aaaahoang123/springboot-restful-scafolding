package vn.amit.entity.auth

import vn.amit.common.dto.WithTimestampsDto
import vn.amit.common.utils.assignObject

class UserDto() : WithTimestampsDto() {
    var id: Int? = null
    var username: String? = null
    var email: String? = null
    var status: Int? = null

    constructor(user: User): this() {
        assignObject(this, user)
    }
}