package org.dripto.germanpostcodesapi

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.dripto.germanpostcodesapi.util.PostcodeStoreFixtures
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class GermanPostcodesApiApplicationTests {
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @BeforeEach
    fun resetStore() {
        PostcodeStoreFixtures.resetToDefaults()
    }

    @Test
    fun `GET postcodes returns 200 with seeded entries`() {
        val response = restTemplate.getForEntity("/postcodes", Array<GermanPostcode>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body.shouldNotBeNull()
        response.body!!.size shouldBe 3
    }

    @Test
    fun `GET postcodes by postcode returns correct entry`() {
        val response = restTemplate.getForEntity("/postcodes/12107", GermanPostcode::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe GermanPostcode("12107", "Berlin")
    }

    @Test
    fun `GET postcodes by unknown postcode returns 404`() {
        val response = restTemplate.getForEntity("/postcodes/99999", String::class.java)

        response.statusCode shouldBe HttpStatus.NOT_FOUND
    }

    @Test
    fun `POST postcodes followed by GET confirms persistence`() {
        val postResponse =
            restTemplate.postForEntity(
                "/postcodes",
                GermanPostcode("10115", "Berlin Mitte"),
                GermanPostcode::class.java,
            )

        postResponse.statusCode shouldBe HttpStatus.CREATED
        postResponse.body shouldBe GermanPostcode("10115", "Berlin Mitte")

        val getResponse = restTemplate.getForEntity("/postcodes/10115", GermanPostcode::class.java)

        getResponse.statusCode shouldBe HttpStatus.OK
        getResponse.body shouldBe GermanPostcode("10115", "Berlin Mitte")
    }

    @Test
    fun `DELETE existing postcode returns 204 and removes it`() {
        val response =
            restTemplate.exchange(
                "/postcodes/12107",
                HttpMethod.DELETE,
                null,
                Void::class.java,
            )
        response.statusCode shouldBe HttpStatus.NO_CONTENT

        // Verify it's gone
        val getResponse = restTemplate.getForEntity("/postcodes/12107", String::class.java)
        getResponse.statusCode shouldBe HttpStatus.NOT_FOUND
    }

    @Test
    fun `DELETE non-existent postcode returns 404`() {
        val response =
            restTemplate.exchange(
                "/postcodes/99999",
                HttpMethod.DELETE,
                null,
                Void::class.java,
            )
        response.statusCode shouldBe HttpStatus.NOT_FOUND
    }
}
