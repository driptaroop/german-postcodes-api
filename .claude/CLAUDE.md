# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
./gradlew bootRun          # Run the application
./gradlew test             # Run all tests
./gradlew test --tests "org.dripto.germanpostcodesapi.SomeTest"  # Run a single test class
./gradlew build            # Build the project
./gradlew formatKotlin     # Auto-format Kotlin code (also runs automatically via hook)
./gradlew lintKotlin       # Check formatting without fixing
```

## Architecture

This is a Spring Boot 4 / Kotlin REST API (Java 25 toolchain) with four layers:

- **Model** (`model/GermanPostcode.kt`): A `@Entity` data class with `postcode` (PK) and `placename` fields, mapped to the `german_postcode` table.
- **Repository** (`repository/PostcodeRepository.kt`): A `JpaRepository<GermanPostcode, String>` backed by H2 in-memory database. Schema and seed data are managed by Liquibase.
- **Service** (`service/PostcodeService.kt`): `@Service` wrapping the repository with `findAll`, `findById`, `save`, and `deleteById` (returns Boolean).
- **Controller** (`controller/PostcodesController.kt`): A `@RestController` exposing `GET /postcodes`, `GET /postcodes/{postcode}`, `POST /postcodes`, and `DELETE /postcodes/{postcode}`. Delegates to `PostcodeService`.
- **Interceptor** (`interceptor/LoggingInterceptor.kt`): Logs method, URI, remote IP, status, and duration for every request. Registered via `config/WebConfig.kt`.
- **Util** (`util/Logging.kt`): Provides a shared `log` extension property used across all classes — see `.claude/rules/logging.md`.

The database is seeded on startup via Liquibase (`db/changelog/changes/002-seed-data.sql`) with three entries (Berlin, Aachen, Klasdorf) and resets on every restart (H2 in-memory).

## Known Quirks
- Do not modify build.gradle.kts without asking first
- Spring Boot 4 uses non-standard package locations: `TestRestTemplate` is in `org.springframework.boot.resttestclient`, `@WebMvcTest` is in `org.springframework.boot.webmvc.test.autoconfigure`, `@DataJpaTest` is in `org.springframework.boot.data.jpa.test.autoconfigure`, and `@AutoConfigureTestRestTemplate` is in `org.springframework.boot.resttestclient.autoconfigure`.
- For `@WebMvcTest` slices, use `@MockitoBean` (from `org.springframework.test.context.bean.override.mockito`) — Spring Framework 7 replaced `@MockBean` with `@MockitoBean`.
- Tests that touch the database should call `repository.deleteAll()` followed by `repository.saveAll(...)` in `@BeforeEach` to reset state between tests (replaces the old `PostcodeStoreFixtures`).

# Additional Instructions
- git workflow @git-workflow.md
