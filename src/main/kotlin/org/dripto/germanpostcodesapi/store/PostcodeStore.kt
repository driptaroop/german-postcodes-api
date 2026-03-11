package org.dripto.germanpostcodesapi.store

import org.dripto.germanpostcodesapi.model.GermanPostcode

object PostcodeStore {
    val postcodes = mutableMapOf(
        "12107" to GermanPostcode("12107", "Berlin"),
        "52062" to GermanPostcode("52062", "Aachen"),
        "15837" to GermanPostcode("15837", "Klasdorf")
    )
}