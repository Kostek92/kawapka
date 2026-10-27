package com.application.kawapka.ui.home

import com.application.kawapka.data.catalogue.CatalogueCoffee
import com.application.kawapka.data.catalogue.CatalogueRepository
import com.application.kawapka.data.collection.CollectionEntry
import com.application.kawapka.data.collection.CollectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val coffees =
        listOf(
            CatalogueCoffee(id = "a", name = "Alpha"),
            CatalogueCoffee(id = "b", name = "Bravo"),
            CatalogueCoffee(id = "c", name = "Charlie"),
        )

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has no collection members`() =
        runTest {
            val viewModel = HomeViewModel(CatalogueRepository { coffees }, FakeCollectionRepository())

            assertTrue(viewModel.uiState.value.catalogue.none { item -> item.isInCollection })
            assertTrue(viewModel.uiState.value.collection.isEmpty())
        }

    @Test
    fun `adding a coffee marks it as added and puts it in the collection`() =
        runTest {
            val viewModel = HomeViewModel(CatalogueRepository { coffees }, FakeCollectionRepository())

            viewModel.addToCollection("b")

            val state = viewModel.uiState.value
            assertTrue(state.catalogue.first { item -> item.coffee.id == "b" }.isInCollection)
            assertEquals(listOf("b"), state.collection.map { item -> item.coffee.id })
        }

    @Test
    fun `adding the same coffee twice keeps one collection entry`() =
        runTest {
            val repository = FakeCollectionRepository()
            val viewModel = HomeViewModel(CatalogueRepository { coffees }, repository)

            viewModel.addToCollection("a")
            viewModel.addToCollection("a")

            assertEquals(1, repository.entries.value.size)
            assertEquals(1, viewModel.uiState.value.collection.size)
        }

    @Test
    fun `removing a coffee clears membership but keeps the catalogue product`() =
        runTest {
            val viewModel = HomeViewModel(CatalogueRepository { coffees }, FakeCollectionRepository())

            viewModel.addToCollection("a")
            viewModel.removeFromCollection("a")

            val state = viewModel.uiState.value
            assertFalse(state.catalogue.first { item -> item.coffee.id == "a" }.isInCollection)
            assertTrue(state.collection.isEmpty())
            assertEquals(3, state.catalogue.size)
        }

    @Test
    fun `rating can be set changed and cleared`() =
        runTest {
            val repository = FakeCollectionRepository()
            val viewModel = HomeViewModel(CatalogueRepository { coffees }, repository)
            viewModel.addToCollection("a")

            viewModel.setRating("a", 4.5f)
            assertEquals(4.5f, ratingOf(viewModel, "a"))

            viewModel.setRating("a", 2.5f)
            assertEquals(2.5f, ratingOf(viewModel, "a"))

            viewModel.clearRating("a")
            assertNull(ratingOf(viewModel, "a"))
        }

    @Test
    fun `setting a query filters the catalogue and clearing it restores every coffee`() =
        runTest {
            val viewModel = HomeViewModel(CatalogueRepository { coffees }, FakeCollectionRepository())

            viewModel.setCatalogueQuery("AL")

            val filtered = viewModel.uiState.value
            assertEquals("AL", filtered.catalogueQuery)
            assertEquals(listOf("a"), filtered.catalogue.map { item -> item.coffee.id })

            viewModel.setCatalogueQuery("")

            val restored = viewModel.uiState.value
            assertEquals("", restored.catalogueQuery)
            assertEquals(coffees, restored.catalogue.map { item -> item.coffee })
        }

    @Test
    fun `query keeps collection membership and contents`() =
        runTest {
            val viewModel = HomeViewModel(CatalogueRepository { coffees }, FakeCollectionRepository())
            viewModel.addToCollection("a")

            viewModel.setCatalogueQuery("alp")

            val state = viewModel.uiState.value
            assertEquals("alp", state.catalogueQuery)
            assertTrue(state.catalogue.single().isInCollection)
            assertEquals(listOf("a"), state.collection.map { item -> item.coffee.id })
        }

    @Test
    fun `adding a coffee from a filtered result keeps the query and updates membership`() =
        runTest {
            val viewModel = HomeViewModel(CatalogueRepository { coffees }, FakeCollectionRepository())

            viewModel.setCatalogueQuery("bra")
            viewModel.addToCollection("b")

            val state = viewModel.uiState.value
            assertEquals("bra", state.catalogueQuery)
            assertEquals(listOf("b"), state.catalogue.map { item -> item.coffee.id })
            assertTrue(state.catalogue.single().isInCollection)
            assertEquals(listOf("b"), state.collection.map { item -> item.coffee.id })
        }

    @Test
    fun `setting a collection query filters the collection and clearing it restores every coffee`() =
        runTest {
            val viewModel = HomeViewModel(CatalogueRepository { coffees }, FakeCollectionRepository())
            viewModel.addToCollection("a")
            viewModel.addToCollection("b")

            viewModel.setCollectionQuery("BR")

            val filtered = viewModel.uiState.value
            assertEquals("BR", filtered.collectionQuery)
            assertEquals(listOf("b"), filtered.collection.map { item -> item.coffee.id })
            assertEquals(3, filtered.catalogue.size)

            viewModel.setCollectionQuery("")

            val restored = viewModel.uiState.value
            assertEquals("", restored.collectionQuery)
            assertEquals(listOf("a", "b"), restored.collection.map { item -> item.coffee.id })
        }

    @Test
    fun `catalogue and collection queries filter their own tabs independently`() =
        runTest {
            val viewModel = HomeViewModel(CatalogueRepository { coffees }, FakeCollectionRepository())
            viewModel.addToCollection("a")
            viewModel.addToCollection("b")

            viewModel.setCatalogueQuery("alp")
            viewModel.setCollectionQuery("bra")

            val state = viewModel.uiState.value
            assertEquals(listOf("a"), state.catalogue.map { item -> item.coffee.id })
            assertEquals(listOf("b"), state.collection.map { item -> item.coffee.id })
        }

    @Test
    fun `collection entries survive a catalogue repository change`() =
        runTest {
            val repository = FakeCollectionRepository(listOf(CollectionEntry(catalogueId = "a", dateAdded = 10)))
            val viewModel = HomeViewModel(CatalogueRepository { coffees }, repository)

            assertTrue(viewModel.uiState.value.catalogue.first { item -> item.coffee.id == "a" }.isInCollection)
            assertEquals(1, repository.entries.value.size)
        }
}

private class FakeCollectionRepository(
    initialEntries: List<CollectionEntry> = emptyList(),
) : CollectionRepository {
    override val entries = MutableStateFlow(initialEntries)
    private var nextDateAdded = 100L

    override suspend fun add(catalogueId: String) {
        entries.update { current ->
            if (current.any { entry -> entry.catalogueId == catalogueId }) {
                current
            } else {
                current + CollectionEntry(catalogueId = catalogueId, dateAdded = nextDateAdded++)
            }
        }
    }

    override suspend fun remove(catalogueId: String) {
        entries.update { current -> current.filterNot { entry -> entry.catalogueId == catalogueId } }
    }

    override suspend fun setRating(
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

private fun ratingOf(
    viewModel: HomeViewModel,
    catalogueId: String,
): Float? = viewModel.uiState.value.collection.first { item -> item.coffee.id == catalogueId }.rating
