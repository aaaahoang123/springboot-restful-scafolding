package vn.amit.common.language

interface LocaleService {
    fun getMessage(key: String, args: Array<Any>? = null): String
    fun getMessage(value: Any, baseClass: Class<*>): String
}