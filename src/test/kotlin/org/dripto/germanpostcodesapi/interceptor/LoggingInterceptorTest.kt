package org.dripto.germanpostcodesapi.interceptor

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Test

class LoggingInterceptorTest {
    private val interceptor = LoggingInterceptor()
    private val request = mockk<HttpServletRequest>()
    private val response = mockk<HttpServletResponse>()
    private val handler = mockk<Any>()

    @Test
    fun `preHandle sets requestStartTime attribute on request`() {
        every { request.method } returns "GET"
        every { request.requestURI } returns "/postcodes"
        every { request.remoteAddr } returns "127.0.0.1"
        every { request.setAttribute(any(), any()) } just runs

        interceptor.preHandle(request, response, handler)

        verify { request.setAttribute("requestStartTime", any<Long>()) }
    }

    @Test
    fun `preHandle returns true`() {
        every { request.method } returns "GET"
        every { request.requestURI } returns "/postcodes"
        every { request.remoteAddr } returns "127.0.0.1"
        every { request.setAttribute(any(), any()) } just runs

        val result = interceptor.preHandle(request, response, handler)

        result shouldBe true
    }

    @Test
    fun `afterCompletion without exception logs completion without error`() {
        val startTime = System.currentTimeMillis() - 100L
        every { request.getAttribute("requestStartTime") } returns startTime
        every { request.method } returns "GET"
        every { request.requestURI } returns "/postcodes"
        every { response.status } returns 200

        interceptor.afterCompletion(request, response, handler, null)
    }

    @Test
    fun `afterCompletion with exception also logs error line`() {
        val startTime = System.currentTimeMillis() - 50L
        every { request.getAttribute("requestStartTime") } returns startTime
        every { request.method } returns "POST"
        every { request.requestURI } returns "/postcodes"
        every { response.status } returns 500

        interceptor.afterCompletion(request, response, handler, RuntimeException("Something went wrong"))
    }

    @Test
    fun `preHandle stores start time close to current time`() {
        val capturedValue = slot<Long>()
        every { request.method } returns "DELETE"
        every { request.requestURI } returns "/postcodes/12107"
        every { request.remoteAddr } returns "10.0.0.1"
        every { request.setAttribute(any(), capture(capturedValue)) } just runs

        val before = System.currentTimeMillis()
        interceptor.preHandle(request, response, handler)
        val after = System.currentTimeMillis()

        capturedValue.captured shouldBe (capturedValue.captured.coerceIn(before, after))
    }
}
