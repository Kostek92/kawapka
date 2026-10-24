package com.application.kawapka.data.collection

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room representation of [CollectionEntry].
 *
 * The catalogue id is the primary key, so the same catalogue coffee can never
 * be added twice.
 */
@Entity(tableName = "collection_entries")
data class CollectionEntryEntity(
    @PrimaryKey val catalogueId: String,
    val dateAdded: Long,
)
