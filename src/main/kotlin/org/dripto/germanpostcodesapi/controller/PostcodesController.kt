package org.dripto.germanpostcodesapi.controller

import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.dripto.germanpostcodesapi.service.PostcodeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class PostcodesController(
    private val postcodeService: PostcodeService,
) {
    @GetMapping("/postcodes")
    fun getAllPostcodes() = postcodeService.findAll()

    @GetMapping("/postcodes/{postcode}")
    fun getPostcode(
        @PathVariable postcode: String,
    ) = postcodeService.findById(postcode) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Postcode $postcode not found")

    @PostMapping("/postcodes")
    @ResponseStatus(HttpStatus.CREATED)
    fun savePostcode(
        @RequestBody postcode: GermanPostcode,
    ): GermanPostcode = postcodeService.save(postcode)

    @DeleteMapping("/postcodes/{postcode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePostcode(
        @PathVariable postcode: String,
    ) {
        if (!postcodeService.deleteById(postcode)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Postcode $postcode not found")
        }
    }
}
