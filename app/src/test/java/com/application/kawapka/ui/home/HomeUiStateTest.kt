package com.application.kawapka.ui.home

import com.application.kawapka.data.catalogue.CatalogueCoffee
import com.application.kawapka.data.collection.CollectionEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeUiStateTest {
    private val coffees =
        listOf(
            CatalogueCoffee(id = "a", name = "Alpha"),
            CatalogueCoffee(id = "b", name = "Bravo"),
            CatalogueCoffee(id = "c", name = "Charlie"),
        )

    @Test
    fun `empty collection leaves every catalogue coffee unadded`() {
        val state = buildHomeUiState(coffees, emptyList())

        assertEquals(coffees, state.catalogue.map { item -> item.coffee })
        assertTrue(state.catalogue.none { item -> item.isInCollection })
        assertTrue(state.collection.isEmpty())
    }

    @Test
    fun `collection entries mark matching catalogue coffees as added`() {
        val state =
            buildHomeUiState(
                coffees,
                listOf(
                    CollectionEntry(catalogueId = "a", dateAdded = 10),
                    CollectionEntry(catalogueId = "c", dateAdded = 20),
                ),
            )

        assertEquals(
            mapOf("a" to true, "b" to false, "c" to true),
            state.catalogue.associate { item -> item.coffee.id to item.isInCollection },
        )
    }

    @Test
    fun `collection follows repository entry order`() {
        val state =
            buildHomeUiState(
                coffees,
                listOf(
                    CollectionEntry(catalogueId = "b", dateAdded = 10),
                    CollectionEntry(catalogueId = "c", dateAdded = 30),
                    CollectionEntry(catalogueId = "a", dateAdded = 10),
                ),
            )

        assertEquals(listOf("b", "c", "a"), state.collection.map { item -> item.coffee.id })
        assertFalse(state.collection.any { item -> item.coffee.id == "missing" })
    }

    @Test
    fun `entries for coffees missing from the catalogue are ignored`() {
        val state =
            buildHomeUiState(
                coffees,
                listOf(
                    CollectionEntry(catalogueId = "a", dateAdded = 10),
                    CollectionEntry(catalogueId = "missing", dateAdded = 20),
                ),
            )

        assertEquals(listOf("a"), state.collection.map { item -> item.coffee.id })
        assertEquals(3, state.catalogue.size)
    }

    @Test
    fun `duplicate collection entries produce one card`() {
        val state =
            buildHomeUiState(
                coffees,
                listOf(
                    CollectionEntry(catalogueId = "a", dateAdded = 10),
                    CollectionEntry(catalogueId = "a", dateAdded = 20),
                ),
            )

        assertEquals(listOf("a"), state.collection.map { item -> item.coffee.id })
        assertTrue(state.catalogue.first { item -> item.coffee.id == "a" }.isInCollection)
    }

    @Test
    fun `ratings follow their collection entries and unrated stays null`() {
        val state =
            buildHomeUiState(
                coffees,
                listOf(
                    CollectionEntry(catalogueId = "a", dateAdded = 10, rating = 4.5f),
                    CollectionEntry(catalogueId = "b", dateAdded = 20),
                ),
            )

        assertEquals(
            mapOf("a" to 4.5f, "b" to null),
            state.collection.associate { item -> item.coffee.id to item.rating },
        )
    }

    @Test
    fun `catalogue query filters the catalogue but leaves the collection untouched`() {
        val state =
            buildHomeUiState(
                catalogue = coffees,
                entries = listOf(CollectionEntry(catalogueId = "c", dateAdded = 10)),
                catalogueQuery = "br",
            )

        assertEquals("br", state.catalogueQuery)
        assertEquals(listOf("b"), state.catalogue.map { item -> item.coffee.id })
        assertEquals(listOf("c"), state.collection.map { item -> item.coffee.id })
    }

    @Test
    fun `collection query filters the collection but leaves the catalogue untouched`() {
        val state =
            buildHomeUiState(
                catalogue = coffees,
                entries =
                    listOf(
                        CollectionEntry(catalogueId = "a", dateAdded = 10),
                        CollectionEntry(catalogueId = "b", dateAdded = 20),
                    ),
                collectionQuery = "br",
            )

        assertEquals("br", state.collectionQuery)
        assertEquals(listOf("b"), state.collection.map { item -> item.coffee.id })
        assertEquals(coffees, state.catalogue.map { item -> item.coffee })
    }

    @Test
    fun `blank collection query returns the whole collection`() {
        val state =
            buildHomeUiState(
                catalogue = coffees,
                entries =
                    listOf(
                        CollectionEntry(catalogueId = "a", dateAdded = 10),
                        CollectionEntry(catalogueId = "b", dateAdded = 20),
                    ),
                collectionQuery = "   ",
            )

        assertEquals(listOf("a", "b"), state.collection.map { item -> item.coffee.id })
    }

    @Test
    fun `collection query with no matches yields an empty collection`() {
        val state =
            buildHomeUiState(
                catalogue = coffees,
                entries = listOf(CollectionEntry(catalogueId = "a", dateAdded = 10)),
                collectionQuery = "zulu",
            )

        assertTrue(state.collection.isEmpty())
        assertEquals(3, state.catalogue.size)
    }

    @Test
    fun `catalogue and collection queries are independent`() {
        val state =
            buildHomeUiState(
                catalogue = coffees,
                entries =
                    listOf(
                        CollectionEntry(catalogueId = "a", dateAdded = 10),
                        CollectionEntry(catalogueId = "b", dateAdded = 20),
                    ),
                catalogueQuery = "alp",
                collectionQuery = "bra",
            )

        assertEquals(listOf("a"), state.catalogue.map { item -> item.coffee.id })
        assertEquals(listOf("b"), state.collection.map { item -> item.coffee.id })
    }

    @Test
    fun `empty catalogue is safe`() {
        val state =
            buildHomeUiState(
                catalogue = emptyList(),
                entries = listOf(CollectionEntry(catalogueId = "a", dateAdded = 10)),
            )

        assertTrue(state.catalogue.isEmpty())
        assertTrue(state.collection.isEmpty())
    }
}
