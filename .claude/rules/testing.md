---
paths:
  - "src/test/**/*.{kt,yaml,yml,properties,xml}"
---

## Testing: 

- JUnit 5 + MockK + kotest (for assertions); min 80% coverage on new code
- There should be 3 types of tests:
  - Unit Tests: Only tests one class/method/function/unit piece of the code. In the unit test it is allowed to mock downstream invocations. All different outcomes of the code under test should be covered.
  - Integration Tests: Integration tests test the interaction of the code with external components. Examples include testing the endpoints of a controller or testing the repository for database interaction. In the test it is preferable that the required external components can be replaced with a test container. In case a test container based test setup is not possible, in memory or embedded components can be used instead. Upstream or downstream function/method calls can be mocked or stubbed as necessary.
  - Functional Tests: Treats the entire application as a black box and tests the full end to end functionality. Test container based components can be used similar to Integration tests.
