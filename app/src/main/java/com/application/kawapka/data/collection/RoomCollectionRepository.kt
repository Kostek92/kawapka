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
                    rating = entity.rating,
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

    override suspend fun setRating(
        catalogueId: String,
        rating: Float?,
    ) {
        require(rating == null || isValidRating(rating)) {
            "Rating must be null or a half-star value between $MIN_RATING and $MAX_RATING"
        }
        dao.updateRating(catalogueId, rating)
    }

    companion object {
        const val MIN_RATING = 0.5f
        const val MAX_RATING = 5f
        const val RATING_STEP = 0.5f

        fun isValidRating(rating: Float): Boolean = rating in MIN_RATING..MAX_RATING && rating % RATING_STEP == 0f
    }
}
