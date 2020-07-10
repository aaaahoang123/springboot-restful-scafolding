package vn.amit.common.language

import org.springframework.http.HttpHeaders
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.*
import javax.servlet.http.HttpServletRequest

class HeaderLocaleResolver: AcceptHeaderLocaleResolver() {
    private val _supportedLocale = listOf("en", "vi")
            .map { lang -> Locale(lang) }

    override fun resolveLocale(req: HttpServletRequest): Locale {
        val language = req.getHeader(HttpHeaders.ACCEPT_LANGUAGE)
        val list = language?.let { it -> Locale.LanguageRange.parse(it) }  ?: listOf()
        return Locale.lookup(list, _supportedLocale) ?: defaultLocale ?: Locale("en")
    }
}