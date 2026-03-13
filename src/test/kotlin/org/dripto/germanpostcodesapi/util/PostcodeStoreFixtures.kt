package org.dripto.germanpostcodesapi.util

import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.dripto.germanpostcodesapi.store.PostcodeStore

object PostcodeStoreFixtures {
    fun resetToDefaults() {
        PostcodeStore.postcodes.clear()
        PostcodeStore.postcodes["12107"] = GermanPostcode("12107", "Berlin")
        PostcodeStore.postcodes["52062"] = GermanPostcode("52062", "Aachen")
        PostcodeStore.postcodes["15837"] = GermanPostcode("15837", "Klasdorf")
    }
}
