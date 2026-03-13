package org.dripto.germanpostcodesapi.service

import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.dripto.germanpostcodesapi.repository.PostcodeRepository
import org.springframework.stereotype.Service

/** Service layer providing business operations over [GermanPostcode] persistence. */
@Service
class PostcodeService(
    private val repository: PostcodeRepository,
) {
    fun findAll(): List<GermanPostcode> = repository.findAll()

    fun findById(postcode: String): GermanPostcode? = repository.findById(postcode).orElse(null)

    fun save(postcode: GermanPostcode): GermanPostcode = repository.save(postcode)

    /**
     * Deletes the entry for the given [postcode].
     * @return `true` if the entry existed and was deleted, `false` if it was not found.
     */
    fun deleteById(postcode: String): Boolean {
        if (!repository.existsById(postcode)) return false
        repository.deleteById(postcode)
        return true
    }
}
