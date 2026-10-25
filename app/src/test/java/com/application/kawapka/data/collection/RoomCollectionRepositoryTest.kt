package com.application.kawapka.data.collection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RoomCollectionRepositoryTest {
    private val dao = FakeCollectionDao()

    @Test
    fun `adding a coffee stores an unrated entry with the injected date`() =
        runTest {
            val repository = RoomCollectionRepository(dao, currentTimeMillis = { 42L })

            repository.add("a")

            val entry = repository.entries.first().single()
            assertEquals("a", entry.catalogueId)
            assertEquals(42L, entry.dateAdded)
            assertNull(entry.rating)
        }

    @Test
    fun `rating can be set changed and cleared`() =
        runTest {
            val repository = RoomCollectionRepository(dao, currentTimeMillis = { 42L })
            repository.add("a")

            repository.setRating("a", 4.5f)
            assertEquals(4.5f, repository.entries.first().single().rating)

            repository.setRating("a", 2.5f)
            assertEquals(2.5f, repository.entries.first().single().rating)

            repository.setRating("a", null)
            assertNull(repository.entries.first().single().rating)
        }

    @Test
    fun `all half star ratings between half and five are accepted`() =
        runTest {
            val repository = RoomCollectionRepository(dao, currentTimeMillis = { 42L })
            repository.add("a")

            listOf(0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f).forEach { rating ->
                repository.setRating("a", rating)
                assertEquals(rating, repository.entries.first().single().rating)
            }
        }

    @Test
    fun `ratings that are not half steps in half to five are rejected`() =
        runTest {
            val repository = RoomCollectionRepository(dao, currentTimeMillis = { 42L })
            repository.add("a")

            listOf(0f, 5.5f, 6f, -1f, 4.25f, Float.NaN).forEach { rating ->
                val error = runCatching { repository.setRating("a", rating) }.exceptionOrNull()
                assertTrue(error is IllegalArgumentException)
            }
            assertNull(repository.entries.first().single().rating)
        }
}

private class FakeCollectionDao : CollectionDao {
    private val entries = MutableStateFlow<List<CollectionEntryEntity>>(emptyList())

    override fun observeEntries(): Flow<List<CollectionEntryEntity>> = entries

    override suspend fun insert(entry: CollectionEntryEntity) {
        entries.update { current ->
            if (current.any { it.catalogueId == entry.catalogueId }) current else current + entry
        }
    }

    override suspend fun delete(catalogueId: String) {
        entries.update { current -> current.filterNot { it.catalogueId == catalogueId } }
    }

    override suspend fun updateRating(
        catalogueId: String,
        rating: Float?,
    ) {
        entries.update { current ->
            current.map { entry ->
                if (entry.catalogueId == catalogueId) entry.copy(rating = rating) else entry
            }
        }
    }
}
