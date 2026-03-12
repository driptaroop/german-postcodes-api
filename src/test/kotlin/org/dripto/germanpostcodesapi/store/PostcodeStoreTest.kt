package org.dripto.germanpostcodesapi.store

import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.dripto.germanpostcodesapi.util.PostcodeStoreFixtures
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PostcodeStoreTest {
    @BeforeEach
    fun resetStore() {
        PostcodeStoreFixtures.resetToDefaults()
    }

    @Test
    fun `store contains the three reset entries at test start`() {
        PostcodeStore.postcodes shouldHaveSize 3
        PostcodeStore.postcodes shouldContainKey "12107"
        PostcodeStore.postcodes shouldContainKey "52062"
        PostcodeStore.postcodes shouldContainKey "15837"
        PostcodeStore.postcodes["12107"] shouldBe GermanPostcode("12107", "Berlin")
        PostcodeStore.postcodes["52062"] shouldBe GermanPostcode("52062", "Aachen")
        PostcodeStore.postcodes["15837"] shouldBe GermanPostcode("15837", "Klasdorf")
    }

    @Test
    fun `adding a new postcode makes it retrievable`() {
        PostcodeStore.postcodes["10115"] = GermanPostcode("10115", "Berlin Mitte")

        PostcodeStore.postcodes["10115"] shouldBe GermanPostcode("10115", "Berlin Mitte")
    }

    @Test
    fun `overwriting an existing postcode returns the updated value`() {
        PostcodeStore.postcodes["12107"] = GermanPostcode("12107", "Berlin Updated")

        PostcodeStore.postcodes["12107"] shouldBe GermanPostcode("12107", "Berlin Updated")
    }

    @Test
    fun `removing a postcode returns null on lookup`() {
        PostcodeStore.postcodes.remove("52062")

        PostcodeStore.postcodes["52062"].shouldBeNull()
    }
}
