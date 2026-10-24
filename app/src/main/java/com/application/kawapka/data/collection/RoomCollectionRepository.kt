package com.application.kawapka.data.collection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room-backed collection storage.
 *
 * [currentTimeMillis] is injectable so date-added behaviour can be tested
 * without relying on the wall clock.
 */
class RoomCollectionRepository(
    private val dao: CollectionDao,
    private val currentTimeMillis: () -> Long = System::currentTimeMillis,
) : CollectionRepository {
    override val entries: Flow<List<CollectionEntry>> =
        dao.observeEntries().map { entities ->
            entities.map { entity ->
                CollectionEntry(
                    catalogueId = entity.catalogueId,
                    dateAdded = entity.dateAdded,
                )
            }
        }

    override suspend fun add(catalogueId: String) {
        dao.insert(
            CollectionEntryEntity(
                catalogueId = catalogueId,
                dateAdded = currentTimeMillis(),
            ),
        )
    }

    override suspend fun remove(catalogueId: String) {
        dao.delete(catalogueId)
    }
}
