package com.application.kawapka.data.collection

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collection_entries")
    fun observeEntries(): Flow<List<CollectionEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: CollectionEntryEntity)

    @Query("DELETE FROM collection_entries WHERE catalogueId = :catalogueId")
    suspend fun delete(catalogueId: String)

    @Query("UPDATE collection_entries SET rating = :rating WHERE catalogueId = :catalogueId")
    suspend fun updateRating(
        catalogueId: String,
        rating: Float?,
    )
}
