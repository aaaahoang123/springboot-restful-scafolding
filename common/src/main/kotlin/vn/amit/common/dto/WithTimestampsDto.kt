package vn.amit.common.dto

import vn.amit.common.annotation.FormattedDateTime

abstract class WithTimestampsDto {
    @FormattedDateTime
    var createdAt: String? = null
    @FormattedDateTime
    var updatedAt: String? = null
}