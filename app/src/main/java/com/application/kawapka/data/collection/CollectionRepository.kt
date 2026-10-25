package com.application.kawapka.data.collection

import kotlinx.coroutines.flow.Flow

/**
 * Personal collection membership and choices.
 *
 * Collection entries store the catalogue id plus personal state; product
 * information stays in [com.application.kawapka.data.catalogue.CatalogueRepository].
 */
interface CollectionRepository {
    val entries: Flow<List<CollectionEntry>>

    suspend fun add(catalogueId: String)

    suspend fun remove(catalogueId: String)

    /**
     * Sets, changes, or clears a coffee's rating.
     *
     * Passing `null` clears the rating and returns the coffee to the unrated
     * state; it never records zero stars.
     */
    suspend fun setRating(
        catalogueId: String,
        rating: Int?,
    )
}
