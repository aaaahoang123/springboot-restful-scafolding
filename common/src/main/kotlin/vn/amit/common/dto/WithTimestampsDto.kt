package vn.amit.common.dto

import vn.amit.common.annotation.FormattedDateTime

abstract class WithTimestampsDto {
    @FormattedDateTime
    var createdDate: String? = null
    @FormattedDateTime
    var updatedDate: String? = null
}