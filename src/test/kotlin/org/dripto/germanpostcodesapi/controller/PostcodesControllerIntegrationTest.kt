package org.dripto.germanpostcodesapi.controller

import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.dripto.germanpostcodesapi.store.PostcodeStore
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import jakarta.servlet.ServletException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(PostcodesController::class)
class PostcodesControllerIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun resetStore() {
        PostcodeStore.postcodes.clear()
        PostcodeStore.postcodes["12107"] = GermanPostcode("12107", "Berlin")
        PostcodeStore.postcodes["52062"] = GermanPostcode("52062", "Aachen")
        PostcodeStore.postcodes["15837"] = GermanPostcode("15837", "Klasdorf")
    }

    // GET /postcodes

    @Test
    fun `GET postcodes returns all seeded entries`() {
        mockMvc.get("/postcodes") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.length()") { value(3) }
        }
    }

    @Test
    fun `GET postcodes returns correct entry data`() {
        mockMvc.get("/postcodes") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            jsonPath("$[?(@.postcode == '12107')].placename") { value("Berlin") }
            jsonPath("$[?(@.postcode == '52062')].placename") { value("Aachen") }
            jsonPath("$[?(@.postcode == '15837')].placename") { value("Klasdorf") }
        }
    }

    @Test
    fun `GET postcodes returns empty list when store is empty`() {
        PostcodeStore.postcodes.clear()

        mockMvc.get("/postcodes") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(0) }
        }
    }

    // GET /postcodes/{postcode}

    @Test
    fun `GET postcodes by postcode returns matching entry`() {
        mockMvc.get("/postcodes/52062") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.postcode") { value("52062") }
            jsonPath("$.placename") { value("Aachen") }
        }
    }

    @Test
    fun `GET postcodes by unknown postcode throws for missing key`() {
        val ex = shouldThrow<ServletException> {
            mockMvc.get("/postcodes/99999").andReturn()
        }
        ex.cause.shouldBeInstanceOf<NoSuchElementException>()
    }

    // POST /postcodes

    @Test
    fun `POST postcodes saves new entry and returns it`() {
        mockMvc.post("/postcodes") {
            param("postcode", "10115")
            param("placename", "Berlin Mitte")
        }.andExpect {
            status { isOk() }
            jsonPath("$.postcode") { value("10115") }
            jsonPath("$.placename") { value("Berlin Mitte") }
        }
    }

    @Test
    fun `POST postcodes persists entry so it can be retrieved`() {
        mockMvc.post("/postcodes") {
            param("postcode", "10115")
            param("placename", "Berlin Mitte")
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/postcodes/10115") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.postcode") { value("10115") }
            jsonPath("$.placename") { value("Berlin Mitte") }
        }
    }

    @Test
    fun `POST postcodes overwrites existing entry`() {
        mockMvc.post("/postcodes") {
            param("postcode", "12107")
            param("placename", "Berlin Updated")
        }.andExpect {
            status { isOk() }
            jsonPath("$.placename") { value("Berlin Updated") }
        }

        mockMvc.get("/postcodes/12107") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            jsonPath("$.placename") { value("Berlin Updated") }
        }
    }
}
