# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
./gradlew bootRun          # Run the application
./gradlew test             # Run all tests
./gradlew test --tests "org.dripto.germanpostcodesapi.SomeTest"  # Run a single test class
./gradlew build            # Build the project
```

## Architecture

This is a Spring Boot 4 / Kotlin REST API (Java 25 toolchain) with three layers:

- **Model** (`model/GermanPostcode.kt`): A simple `data class` with `postcode` and `placename` fields.
- **Store** (`store/PostcodeStore.kt`): A Kotlin `object` (singleton) holding an in-memory `MutableMap`. No database is used despite JPA/H2 being on the classpath — persistence is currently intentionally absent.
- **Controller** (`controller/PostcodesController.kt`): A `@RestController` exposing `GET /postcodes`, `GET /postcodes/{postcode}`, and `POST /postcodes`. Reads and writes directly to `PostcodeStore`.

The store is pre-seeded with three entries (Berlin, Aachen, Klasdorf) and resets on every restart.

## Coding Standards
- Kotlin: follow official style guide; prefer data classes over POJOs
- No Lombok — we use Kotlin instead
- Tests: JUnit 5 + MockK + kotest (for assertions); min 80% coverage on new code

## Git Workflow
- Branch: feat/short-description
- Commits: Conventional Commits format (feat:, fix:, refactor:)

## Known Quirks
- Do not modify build.gradle.kts without asking first

