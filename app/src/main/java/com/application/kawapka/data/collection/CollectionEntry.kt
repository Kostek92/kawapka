package com.application.kawapka.data.collection

/**
 * One coffee in a person's personal collection.
 *
 * Personal state is keyed by the stable catalogue id so the bundled catalogue
 * can be replaced independently. [dateAdded] is stored as epoch milliseconds
 * and is used to keep the collection in a stable order.
 */
data class CollectionEntry(
    val catalogueId: String,
    val dateAdded: Long,
)
