package org.dripto.germanpostcodesapi.service

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.dripto.germanpostcodesapi.repository.PostcodeRepository
import org.junit.jupiter.api.Test
import java.util.Optional

class PostcodeServiceTest {
    private val repository = mockk<PostcodeRepository>()
    private val service = PostcodeService(repository)

    @Test
    fun `findAll delegates to repository and returns list`() {
        val postcodes =
            listOf(
                GermanPostcode("12107", "Berlin"),
                GermanPostcode("52062", "Aachen"),
            )
        every { repository.findAll() } returns postcodes

        service.findAll() shouldBe postcodes
    }

    @Test
    fun `findById returns entity when it exists`() {
        val postcode = GermanPostcode("12107", "Berlin")
        every { repository.findById("12107") } returns Optional.of(postcode)

        service.findById("12107") shouldBe postcode
    }

    @Test
    fun `findById returns null when entity does not exist`() {
        every { repository.findById("99999") } returns Optional.empty()

        service.findById("99999") shouldBe null
    }

    @Test
    fun `save delegates to repository and returns saved entity`() {
        val postcode = GermanPostcode("10115", "Berlin Mitte")
        every { repository.save(postcode) } returns postcode

        service.save(postcode) shouldBe postcode
    }

    @Test
    fun `deleteById returns true and deletes when entry exists`() {
        every { repository.existsById("12107") } returns true
        every { repository.deleteById("12107") } returns Unit

        service.deleteById("12107") shouldBe true
        verify { repository.deleteById("12107") }
    }

    @Test
    fun `deleteById returns false without deleting when entry does not exist`() {
        every { repository.existsById("99999") } returns false

        service.deleteById("99999") shouldBe false
        verify(exactly = 0) { repository.deleteById(any()) }
    }
}
