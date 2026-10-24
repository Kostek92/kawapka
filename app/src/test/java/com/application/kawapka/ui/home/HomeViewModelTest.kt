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
            assertEquals(listOf("b"), state.collection.map { coffee -> coffee.id })
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
}
