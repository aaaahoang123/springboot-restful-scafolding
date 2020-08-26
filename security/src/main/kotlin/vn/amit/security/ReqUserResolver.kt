package vn.amit.security

import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.core.MethodParameter
import org.springframework.web.method.support.HandlerMethodArgumentResolver


class ReqUserResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(methodParameter: MethodParameter): Boolean {
        return methodParameter.getParameterAnnotation(ReqUser::class.java) != null
    }

    @Throws(Exception::class)
    override fun resolveArgument(
            methodParameter: MethodParameter,
            modelAndViewContainer: ModelAndViewContainer?,
            nativeWebRequest: NativeWebRequest,
            webDataBinderFactory: WebDataBinderFactory?): Any? {

        val request = nativeWebRequest.nativeRequest as HttpServletRequest
        return request.getAttribute(USER_ATTR_NAME)
    }
}