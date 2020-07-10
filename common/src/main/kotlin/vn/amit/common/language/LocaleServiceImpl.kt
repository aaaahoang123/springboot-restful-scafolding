package vn.amit.common.language

import org.apache.commons.text.WordUtils
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.servlet.LocaleResolver
import java.util.*
import org.springframework.context.annotation.ScopedProxyMode

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
class LocaleServiceImpl: LocaleService {
    @Autowired
    private lateinit var messageSource: MessageSource
    @Autowired
    private lateinit var localeResolver: LocaleResolver
    @Autowired
    private lateinit var req: HttpServletRequest

    private var locale: Locale? = null

    override fun getMessage(key: String, args: Array<Any>?): String {
        val locale = getLocale()
        val trueArg = args?.map { arg ->
            if (arg is String) {
                getMessage(arg, null)
            } else {
                arg
            }
        }?.toTypedArray()
        return messageSource.getMessage(key, trueArg, WordUtils.capitalize(key), locale)!!
    }

    /**
     * Get locale message from an custom enum
     */
    override fun getMessage(value: Any, baseClass: Class<*>): String {
        val key = getEnumLocalKey(value, baseClass)
        val locale = getLocale()
        return messageSource.getMessage(key, null, key, locale)!!
    }

    private fun getLocale(): Locale {
        return locale ?: run { locale = localeResolver.resolveLocale(req); return locale!! }
    }

    private fun getEnumLocalKey(value: Any, baseClass: Class<*>): String {
        return baseClass.name + "." + value
    }
}