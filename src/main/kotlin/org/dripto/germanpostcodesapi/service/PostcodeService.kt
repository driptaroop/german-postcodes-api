package org.dripto.germanpostcodesapi.service

import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.dripto.germanpostcodesapi.repository.PostcodeRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Service layer providing business operations over [GermanPostcode] persistence. */
@Service
class PostcodeService(
    private val repository: PostcodeRepository,
) {
    fun findAll(): List<GermanPostcode> = repository.findAll()

    fun findById(postcode: String): GermanPostcode? = repository.findByIdOrNull(postcode)

    fun save(postcode: GermanPostcode): GermanPostcode = repository.save(postcode)

    /**
     * Deletes the entry for the given [postcode].
     * @return `true` if the entry existed and was deleted, `false` if it was not found.
     */
    @Transactional
    fun deleteById(postcode: String): Boolean {
        if (!repository.existsById(postcode)) return false
        repository.deleteById(postcode)
        return true
    }
}
