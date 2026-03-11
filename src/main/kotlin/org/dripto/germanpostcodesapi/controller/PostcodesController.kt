package org.dripto.germanpostcodesapi.controller

import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.dripto.germanpostcodesapi.store.PostcodeStore
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class PostcodesController {
    @GetMapping("/postcodes")
    fun getAllPostcodes() = PostcodeStore.postcodes.values.toList()

    @GetMapping("/postcodes/{postcode}")
    fun getPostcode(
        @PathVariable postcode: String,
    ) = PostcodeStore.postcodes[postcode] ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PostMapping("/postcodes")
    @ResponseStatus(HttpStatus.CREATED)
    fun savePostcode(
        @RequestBody postcode: GermanPostcode,
    ): GermanPostcode {
        PostcodeStore.postcodes[postcode.postcode] = postcode
        return postcode
    }
}
