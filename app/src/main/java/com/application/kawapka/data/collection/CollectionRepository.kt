package com.application.kawapka.data.collection

import kotlinx.coroutines.flow.Flow

/**
 * Personal collection membership.
 *
 * Only catalogue ids are stored; product information stays in
 * [com.application.kawapka.data.catalogue.CatalogueRepository].
 */
interface CollectionRepository {
    val entries: Flow<List<CollectionEntry>>

    suspend fun add(catalogueId: String)

    suspend fun remove(catalogueId: String)
}
