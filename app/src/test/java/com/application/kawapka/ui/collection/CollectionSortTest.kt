package com.application.kawapka.ui.collection

import com.application.kawapka.data.catalogue.CatalogueCoffee
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CollectionSortTest {
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

        assertEquals(listOf("alpha", "bravo", "charlie", "delta"), ids(sortCollection(items, CollectionSort.Name)))
    }

    @Test
    fun `name sort breaks ties by catalogue id`() {
        val items =
            listOf(
                CollectionItemUiState(coffee = CatalogueCoffee(id = "zulu", name = "Same"), rating = null),
                CollectionItemUiState(coffee = CatalogueCoffee(id = "alpha", name = "Same"), rating = null),
            )

        assertEquals(listOf("alpha", "zulu"), ids(sortCollection(items, CollectionSort.Name)))
    }

    @Test
    fun `intensity sort is highest first and puts missing intensity last`() {
        val items = listOf(item("alpha"), item("bravo"), item("charlie"), item("delta"))

        assertEquals(
            listOf("charlie", "delta", "alpha", "bravo"),
            ids(sortCollection(items, CollectionSort.IntensityDescending)),
        )
    }

    @Test
    fun `rating sort is highest first and puts unrated last`() {
        val items =
            listOf(
                item("alpha", rating = 4.5f),
                item("bravo", rating = null),
                item("charlie", rating = 3f),
                item("delta", rating = 5f),
            )

        assertEquals(
            listOf("delta", "alpha", "charlie", "bravo"),
            ids(sortCollection(items, CollectionSort.RatingDescending)),
        )
    }

    @Test
    fun `rating sort keeps a half star above an unrated coffee`() {
        val items = listOf(item("alpha", rating = 0.5f), item("bravo", rating = null))

        assertEquals(
            listOf("alpha", "bravo"),
            ids(sortCollection(items, CollectionSort.RatingDescending)),
        )
    }

    @Test
    fun `equal ratings break ties by name`() {
        val items =
            listOf(
                item("delta", rating = 4f),
                item("alpha", rating = 4f),
                item("charlie", rating = 4f),
            )

        assertEquals(
            listOf("alpha", "charlie", "delta"),
            ids(sortCollection(items, CollectionSort.RatingDescending)),
        )
    }

    @Test
    fun `empty collection is safe`() {
        CollectionSort.entries.forEach { sort ->
            assertTrue(sortCollection(emptyList(), sort).isEmpty())
        }
    }

    private fun item(
        id: String,
        rating: Float? = null,
    ): CollectionItemUiState = CollectionItemUiState(coffee = coffees.getValue(id), rating = rating)

    private fun ids(items: List<CollectionItemUiState>): List<String> = items.map { item -> item.coffee.id }
}
