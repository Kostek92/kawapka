package com.application.kawapka.data.collection

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Local storage for personal data.
 *
 * The catalogue is not stored here; it remains a bundled read-only snapshot.
 * Schema JSON is exported to `app/schemas` so later phases can add migrations
 * without losing collection entries.
 */
@Database(
    entities = [CollectionEntryEntity::class],
    version = 2,
    exportSchema = true,
)
abstract class KawapkaDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDao

    companion object {
        /** Adds the optional 1–5 rating; existing collection entries stay unrated. */
        val MIGRATION_1_2 =
            object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("ALTER TABLE collection_entries ADD COLUMN rating INTEGER")
                }
            }
    }
}
