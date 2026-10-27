package com.application.kawapka.ui.catalogue

import com.application.kawapka.data.catalogue.CatalogueCoffee
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogueSortTest {
    private val coffees =
        mapOf(
            "alpha" to CatalogueCoffee(id = "alpha", name = "Alpha", intensity = 4),
            "bravo" to CatalogueCoffee(id = "bravo", name = "Bravo"),
            "charlie" to CatalogueCoffee(id = "charlie", name = "Charlie", intensity = 13),
            "delta" to CatalogueCoffee(id = "delta", name = "delta", intensity = 13),
        )

    @Test
    fun `name sort is case insensitive`() {
        val items = listOf(item("delta"), item("bravo"), item("alpha"), item("charlie"))

        assertEquals(listOf("alpha", "bravo", "charlie", "delta"), ids(sortCatalogue(items, CatalogueSort.Name)))
    }

    @Test
    fun `name sort breaks ties by catalogue id`() {
        val items =
            listOf(
                CatalogueItemUiState(coffee = CatalogueCoffee(id = "zulu", name = "Same"), isInCollection = false),
                CatalogueItemUiState(coffee = CatalogueCoffee(id = "alpha", name = "Same"), isInCollection = true),
            )

        assertEquals(listOf("alpha", "zulu"), ids(sortCatalogue(items, CatalogueSort.Name)))
    }

    @Test
    fun `intensity sort is highest first and puts missing intensity last`() {
        val items = listOf(item("alpha"), item("bravo"), item("charlie"), item("delta"))

        assertEquals(
            listOf("charlie", "delta", "alpha", "bravo"),
            ids(sortCatalogue(items, CatalogueSort.IntensityDescending)),
        )
    }

    @Test
    fun `equal intensities break ties by name`() {
        val items =
            listOf(
                CatalogueItemUiState(CatalogueCoffee(id = "delta", name = "Delta", intensity = 9), isInCollection = false),
                CatalogueItemUiState(CatalogueCoffee(id = "alpha", name = "Alpha", intensity = 9), isInCollection = false),
                CatalogueItemUiState(CatalogueCoffee(id = "charlie", name = "Charlie", intensity = 9), isInCollection = false),
            )

        assertEquals(
            listOf("alpha", "charlie", "delta"),
            ids(sortCatalogue(items, CatalogueSort.IntensityDescending)),
        )
    }

    @Test
    fun `sorting preserves collection membership`() {
        val items =
            listOf(
                CatalogueItemUiState(coffee = coffees.getValue("alpha"), isInCollection = false),
                CatalogueItemUiState(coffee = coffees.getValue("charlie"), isInCollection = true),
            )

        assertEquals(
            listOf(false, true),
            sortCatalogue(items, CatalogueSort.Name).map { item -> item.isInCollection },
        )
    }

    @Test
    fun `empty catalogue is safe`() {
        CatalogueSort.entries.forEach { sort ->
            assertTrue(sortCatalogue(emptyList(), sort).isEmpty())
        }
    }

    private fun item(id: String): CatalogueItemUiState = CatalogueItemUiState(coffee = coffees.getValue(id), isInCollection = false)

    private fun ids(items: List<CatalogueItemUiState>): List<String> = items.map { item -> item.coffee.id }
}
