package org.dripto.germanpostcodesapi.repository

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.optional.shouldNotBePresent
import io.kotest.matchers.shouldBe
import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest

@DataJpaTest
class PostcodeRepositoryTest {
    @Autowired
    lateinit var repository: PostcodeRepository

    @BeforeEach
    fun resetDatabase() {
        repository.deleteAll()
        repository.saveAll(
            listOf(
                GermanPostcode("12107", "Berlin"),
                GermanPostcode("52062", "Aachen"),
                GermanPostcode("15837", "Klasdorf"),
            ),
        )
    }

    @Test
    fun `findAll returns the three seeded postcodes`() {
        val all = repository.findAll()
        all shouldHaveSize 3
    }

    @Test
    fun `findById returns matching entry for a seeded postcode`() {
        val result = repository.findById("12107")
        result.shouldBePresent()
        result.get() shouldBe GermanPostcode("12107", "Berlin")
    }

    @Test
    fun `findById returns empty for an unknown postcode`() {
        repository.findById("99999").shouldNotBePresent()
    }

    @Test
    fun `save persists a new entry and it can be retrieved`() {
        val newEntry = GermanPostcode("10115", "Berlin Mitte")
        repository.save(newEntry)

        val retrieved = repository.findById("10115")
        retrieved.shouldBePresent()
        retrieved.get() shouldBe newEntry
    }

    @Test
    fun `save overwrites placename for an existing postcode`() {
        repository.save(GermanPostcode("12107", "Berlin Updated"))

        val retrieved = repository.findById("12107")
        retrieved.shouldBePresent()
        retrieved.get().placename shouldBe "Berlin Updated"
    }

    @Test
    fun `deleteById removes the entry`() {
        repository.deleteById("12107")
        repository.findById("12107").shouldNotBePresent()
    }

    @Test
    fun `existsById returns true for a seeded postcode`() {
        repository.existsById("52062") shouldBe true
    }

    @Test
    fun `existsById returns false for an unknown postcode`() {
        repository.existsById("99999") shouldBe false
    }
}
