package com.application.kawapka.data.collection

/**
 * One coffee in a person's personal collection.
 *
 * Personal state is keyed by the stable catalogue id so the bundled catalogue
 * can be replaced independently. [dateAdded] is stored as epoch milliseconds
 * only for backwards compatibility; it no longer affects ordering and is
 * scheduled for removal in a future database migration. [rating] is a half-star
 * value from 0.5 to 5, or `null` when the coffee has not been rated yet.
 */
data class CollectionEntry(
    val catalogueId: String,
    val dateAdded: Long,
    val rating: Float? = null,
)
