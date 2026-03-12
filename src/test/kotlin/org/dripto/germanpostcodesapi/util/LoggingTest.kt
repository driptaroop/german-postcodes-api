package org.dripto.germanpostcodesapi.util

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoggingTest {
    private class SampleClass {
        fun logger() = log
    }

    private class AnotherClass {
        fun logger() = log
    }

    private class WithCompanion {
        companion object {
            fun logger() = log
        }
    }

    @BeforeEach
    fun clearCache() {
        loggerCache.clear()
    }

    @Test
    fun `log resolves to the correct class name`() {
        val logger = SampleClass().logger()
        logger.name shouldBe SampleClass::class.java.name
    }

    @Test
    fun `companion object log resolves to enclosing class name`() {
        val logger = WithCompanion.logger()
        logger.name shouldBe WithCompanion::class.java.name
    }

    @Test
    fun `same logger instance is returned on repeated calls`() {
        val instance = SampleClass()
        val first = instance.logger()
        val second = instance.logger()
        first shouldBe second
    }

    @Test
    fun `two different classes get different loggers`() {
        val loggerA = SampleClass().logger()
        val loggerB = AnotherClass().logger()
        loggerA shouldNotBe loggerB
    }
}
