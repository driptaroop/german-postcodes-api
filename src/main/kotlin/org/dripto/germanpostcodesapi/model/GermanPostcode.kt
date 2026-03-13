package org.dripto.germanpostcodesapi.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "german_postcode")
data class GermanPostcode(
    @Id
    @Column(name = "postcode")
    val postcode: String,
    @Column(name = "placename", nullable = false)
    val placename: String,
)
