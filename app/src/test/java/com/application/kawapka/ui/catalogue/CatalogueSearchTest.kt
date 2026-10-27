package com.application.kawapka.ui.catalogue

import com.application.kawapka.data.catalogue.CatalogueCoffee
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogueSearchTest {
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
        coffees.map { coffee ->
            CatalogueItemUiState(coffee = coffee, isInCollection = coffee.id == "bravo")
        }

    @Test
    fun `blank query returns every item`() {
        assertEquals(items, filterCatalogue(items, ""))
    }

    @Test
    fun `whitespace-only query returns every item`() {
        assertEquals(items, filterCatalogue(items, "   "))
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
        assertTrue(filterCatalogue(items, "fruity").isEmpty())
        assertTrue(filterCatalogue(items, "bergamot").isEmpty())
        assertTrue(filterCatalogue(items, "floral").isEmpty())
    }

    @Test
    fun `no match returns an empty list`() {
        assertTrue(filterCatalogue(items, "zulu").isEmpty())
    }

    @Test
    fun `membership flags are preserved by filtering`() {
        val filtered = filterCatalogue(items, "bravo")

        assertEquals(listOf("bravo"), filtered.map { item -> item.coffee.id })
        assertTrue(filtered.single().isInCollection)
    }

    private fun idsFor(query: String): List<String> = filterCatalogue(items, query).map { item -> item.coffee.id }
}
