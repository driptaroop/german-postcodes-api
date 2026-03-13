package org.dripto.germanpostcodesapi.controller

import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.dripto.germanpostcodesapi.service.PostcodeService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(PostcodesController::class)
class PostcodesControllerIntegrationTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var postcodeService: PostcodeService

    // GET /postcodes

    @Test
    fun `GET postcodes returns all entries from service`() {
        given(postcodeService.findAll()).willReturn(
            listOf(
                GermanPostcode("12107", "Berlin"),
                GermanPostcode("52062", "Aachen"),
                GermanPostcode("15837", "Klasdorf"),
            ),
        )

        mockMvc
            .get("/postcodes") {
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { value(3) }
            }
    }

    @Test
    fun `GET postcodes returns correct entry data`() {
        given(postcodeService.findAll()).willReturn(
            listOf(
                GermanPostcode("12107", "Berlin"),
                GermanPostcode("52062", "Aachen"),
                GermanPostcode("15837", "Klasdorf"),
            ),
        )

        mockMvc
            .get("/postcodes") {
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                jsonPath("$[?(@.postcode == '12107')].placename") { value("Berlin") }
                jsonPath("$[?(@.postcode == '52062')].placename") { value("Aachen") }
                jsonPath("$[?(@.postcode == '15837')].placename") { value("Klasdorf") }
            }
    }

    @Test
    fun `GET postcodes returns empty list when service returns empty`() {
        given(postcodeService.findAll()).willReturn(emptyList())

        mockMvc
            .get("/postcodes") {
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
                jsonPath("$.length()") { value(0) }
            }
    }

    // GET /postcodes/{postcode}

    @Test
    fun `GET postcodes by postcode returns matching entry`() {
        given(postcodeService.findById("52062")).willReturn(GermanPostcode("52062", "Aachen"))

        mockMvc
            .get("/postcodes/52062") {
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
                jsonPath("$.postcode") { value("52062") }
                jsonPath("$.placename") { value("Aachen") }
            }
    }

    @Test
    fun `GET postcodes by unknown postcode returns 404`() {
        given(postcodeService.findById("99999")).willReturn(null)

        mockMvc
            .get("/postcodes/99999") {
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isNotFound() }
            }
    }

    // POST /postcodes

    @Test
    fun `POST postcodes saves new entry and returns it`() {
        val postcode = GermanPostcode("10115", "Berlin Mitte")
        given(postcodeService.save(postcode)).willReturn(postcode)

        mockMvc
            .post("/postcodes") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"postcode": "10115", "placename": "Berlin Mitte"}"""
            }.andExpect {
                status { isCreated() }
                jsonPath("$.postcode") { value("10115") }
                jsonPath("$.placename") { value("Berlin Mitte") }
            }
    }

    @Test
    fun `POST postcodes overwrites existing entry`() {
        val updated = GermanPostcode("12107", "Berlin Updated")
        given(postcodeService.save(updated)).willReturn(updated)

        mockMvc
            .post("/postcodes") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"postcode": "12107", "placename": "Berlin Updated"}"""
            }.andExpect {
                status { isCreated() }
                jsonPath("$.placename") { value("Berlin Updated") }
            }
    }

    // DELETE /postcodes/{postcode}

    @Test
    fun `DELETE existing postcode returns 204`() {
        given(postcodeService.deleteById("12107")).willReturn(true)

        mockMvc
            .delete("/postcodes/12107")
            .andExpect {
                status { isNoContent() }
            }
    }

    @Test
    fun `DELETE non-existent postcode returns 404`() {
        given(postcodeService.deleteById("99999")).willReturn(false)

        mockMvc
            .delete("/postcodes/99999")
            .andExpect {
                status { isNotFound() }
            }
    }
}
