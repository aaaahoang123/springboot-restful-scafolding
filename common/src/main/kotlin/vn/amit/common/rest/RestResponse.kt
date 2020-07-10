package vn.amit.common.rest

class RestResponse (
        val data: Any? = null,
        val meta: PaginationDto<*>? = null,
        val message: String = "success",
        val status: Int = 1
)