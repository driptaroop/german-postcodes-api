package org.dripto.germanpostcodesapi.repository

import org.dripto.germanpostcodesapi.model.GermanPostcode
import org.springframework.data.jpa.repository.JpaRepository

/** Spring Data JPA repository for [GermanPostcode] entities, keyed by postcode string. */
interface PostcodeRepository : JpaRepository<GermanPostcode, String>
