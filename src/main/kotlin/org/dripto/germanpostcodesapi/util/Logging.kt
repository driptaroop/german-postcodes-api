package org.dripto.germanpostcodesapi.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/** Cache of [Logger] instances keyed by class, populated on first use. */
val loggerCache = mutableMapOf<KClass<*>, Logger>()

/**
 * Inline extension property that provides an SLF4J [Logger] for any class or object
 * without requiring a manual logger declaration.
 *
 * The logger name resolves to the runtime class of the receiver. Companion objects
 * are transparently mapped to their enclosing class.
 *
 * Usage:
 * ```kotlin
 * class MyService {
 *     fun doWork() = log.info("doing work")
 * }
 * object MySingleton {
 *     fun run() = log.debug("running")
 * }
 * ```
 */
inline val <reified T> T.log: Logger
    get() =
        loggerCache.getOrPut(T::class) {
            LoggerFactory.getLogger(if (T::class.isCompanion) T::class.java.enclosingClass else T::class.java)
        }
