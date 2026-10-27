package com.application.kawapka.ui.collection

import com.application.kawapka.data.catalogue.CatalogueCoffee
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CollectionSearchTest {
    private val coffees =
        listOf(
            CatalogueCoffee(
                id = "alpha",
                name = "Alpha",
                headline = "Fruity",
                aromatics = listOf("Bergamot"),
                shortDescription = "A bright floral espresso",
            ),
            CatalogueCoffee(
                id = "bravo",
                name = "Bravo",
                headline = "Chocolate",
                aromatics = listOf("Cocoa"),
                shortDescription = "Roasted and full bodied",
            ),
            CatalogueCoffee(id = "charlie", name = "Charlie"),
        )
    private val items =
        coffees.mapIndexed { index, coffee ->
            CollectionItemUiState(
                coffee = coffee,
                rating = if (index == 1) 4.5f else null,
            )
        }

    @Test
    fun `blank query returns every item`() {
        assertEquals(items, filterCollection(items, ""))
    }

    @Test
    fun `whitespace-only query returns every item`() {
        assertEquals(items, filterCollection(items, "   "))
    }

    @Test
    fun `partial names match with different casing`() {
        assertEquals(listOf("alpha"), idsFor("lph"))
        assertEquals(listOf("alpha"), idsFor("ALPHA"))
        assertEquals(listOf("bravo"), idsFor("raV"))
    }

    @Test
    fun `surrounding whitespace is ignored when matching`() {
        assertEquals(listOf("alpha"), idsFor("  alpha  "))
    }

    @Test
    fun `matches the coffee name only`() {
        assertTrue(filterCollection(items, "fruity").isEmpty())
        assertTrue(filterCollection(items, "bergamot").isEmpty())
        assertTrue(filterCollection(items, "floral").isEmpty())
    }

    @Test
    fun `no match returns an empty list`() {
        assertTrue(filterCollection(items, "zulu").isEmpty())
    }

    @Test
    fun `ratings are preserved by filtering`() {
        val filtered = filterCollection(items, "bravo")

        assertEquals(listOf("bravo"), filtered.map { item -> item.coffee.id })
        assertEquals(4.5f, filtered.single().rating)
    }

    private fun idsFor(query: String): List<String> = filterCollection(items, query).map { item -> item.coffee.id }
}
