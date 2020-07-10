package vn.amit.common.language

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import java.util.*
import javax.validation.Validator


@Configuration
class LocaleConfig : WebMvcConfigurer {
    @Bean
    fun validatorFactory(messageSource: MessageSource?): Validator {
        val validator = LocalValidatorFactoryBean()
        validator.setValidationMessageSource(messageSource!!)

        return validator
    }

    @Bean("messageSource")
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.addBasenames(
                "classpath:locale/messages",
                "classpath:locale/enums",
                "classpath:locale/ValidationMessages",
                "classpath:org/hibernate/validator/ValidationMessages"
        )
//            tempMessageSource
        messageSource.setCacheSeconds(10)
        messageSource.setDefaultEncoding("UTF-8")
//            tempMessageSource.setUseCodeAsDefaultMessage(true)
        return messageSource;
    }

    @Bean
    fun localeResolver(): LocaleResolver? {
        val localeResolver = HeaderLocaleResolver()
        localeResolver.defaultLocale = Locale.Builder().setLanguage("en").build()
        return localeResolver
    }

//    @Bean
//    fun localeChangeInterceptor(): LocaleChangeInterceptor {
//        val lci = LocaleChangeInterceptor()
//        lci.paramName = ""
//        return lci
//    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        val lci = LocaleChangeInterceptor()
        lci.paramName = ""
        registry.addInterceptor(lci)
    }

//    @Bean("multipartResolver")
//    fun multipartResolver(): CommonsMultipartResolver {
//        val multipartResolver = CommonsMultipartResolver()
//        multipartResolver.setMaxUploadSize((25 * 1024 * 1024).toLong())
//        return multipartResolver
//    }


}