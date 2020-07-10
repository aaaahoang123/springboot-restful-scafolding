package vn.amit.common.rest

import org.springframework.data.domain.Page

class PaginationDto<T>(
        pagination: Page<T>,
        val page: Int,
        val limit: Int
) {
    val total = pagination.totalElements
    val totalPage = pagination.totalPages
}