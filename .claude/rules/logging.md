## Logging

Use the shared `log` extension property from `org.dripto.germanpostcodesapi.util.log` — do **not** declare a manual `LoggerFactory.getLogger(...)` field in any class.

```kotlin
import org.dripto.germanpostcodesapi.util.log

class MyService {
    fun doWork() = log.info("doing work")
}
```

This works in any class, object, or companion object without any declaration. Companion objects automatically log under the enclosing class name.
