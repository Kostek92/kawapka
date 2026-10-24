package com.application.kawapka.data.collection

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Local storage for personal data.
 *
 * The catalogue is not stored here; it remains a bundled read-only snapshot.
 * Schema JSON is exported to `app/schemas` so later phases can add migrations
 * without losing collection entries.
 */
@Database(
    entities = [CollectionEntryEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class KawapkaDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDao
}
