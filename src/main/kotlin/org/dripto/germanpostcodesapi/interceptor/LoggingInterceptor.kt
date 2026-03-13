package org.dripto.germanpostcodesapi.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.dripto.germanpostcodesapi.util.log
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class LoggingInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        request.setAttribute(REQUEST_START_ATTR, System.currentTimeMillis())
        log.info("→ {} {} from {}", request.method, request.requestURI, request.remoteAddr)
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        val startTime = request.getAttribute(REQUEST_START_ATTR) as? Long ?: return
        val durationMs = System.currentTimeMillis() - startTime
        log.info("← {} {} {} {}ms", request.method, request.requestURI, response.status, durationMs)
        if (ex != null) {
            log.error("Unhandled exception for {} {}", request.method, request.requestURI, ex)
        }
    }

    companion object {
        internal const val REQUEST_START_ATTR = "requestStartTime"
    }
}
