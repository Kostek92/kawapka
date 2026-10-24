package com.application.kawapka.data.catalogue

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Guards the shape of the generated catalogue snapshot. Regenerate the data with
 * `scripts/generate-catalogue` rather than editing [GeneratedCatalogue] by hand.
 */
class GeneratedCatalogueTest {
    @Test
    fun `snapshot has a useful number of coffees`() {
        assertTrue(GeneratedCatalogue.coffees.size >= 20)
    }

    @Test
    fun `ids are stable Nespresso SKUs and unique`() {
        val ids = GeneratedCatalogue.coffees.map { it.id }

        assertTrue(ids.all { it.matches(Regex("""\d{4}\.\d{2}""")) })
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `every coffee has a clean name`() {
        GeneratedCatalogue.coffees.forEach { coffee ->
            assertTrue(coffee.id, coffee.name.isNotBlank())
            assertFalse(coffee.id, Regex("""\(\s*\d+\s*(?:/\s*\d+\s*)?ml\s*\)$""").containsMatchIn(coffee.name))
        }
    }

    @Test
    fun `short descriptions stay within the generator limit`() {
        GeneratedCatalogue.coffees.forEach { coffee ->
            val description = coffee.shortDescription
            if (!description.isNullOrBlank()) {
                assertTrue(coffee.id, description.length <= 101)
            }
        }
    }

    @Test
    fun `tag data is well formed`() {
        GeneratedCatalogue.coffees.forEach { coffee ->
            coffee.headline?.let { headline ->
                assertTrue(coffee.id, headline.isNotBlank())
                assertEquals(coffee.id, headline.trim(), headline)
            }
            coffee.intensity?.let { intensity ->
                assertTrue(coffee.id, intensity in 1..CatalogueCoffee.MAX_INTENSITY)
            }
        }
    }

    @Test
    fun `most coffees have a headline, intensity, and aroma tags`() {
        val withHeadline = GeneratedCatalogue.coffees.count { !it.headline.isNullOrBlank() }
        val withIntensity = GeneratedCatalogue.coffees.count { it.intensity != null }
        val withAromatics = GeneratedCatalogue.coffees.count { it.aromatics.isNotEmpty() }

        assertTrue(withHeadline >= GeneratedCatalogue.coffees.size / 2)
        assertTrue(withIntensity >= GeneratedCatalogue.coffees.size / 2)
        assertTrue(withAromatics >= GeneratedCatalogue.coffees.size / 2)
    }

    @Test
    fun `aroma tags are clean and unique`() {
        GeneratedCatalogue.coffees.forEach { coffee ->
            assertEquals(coffee.id, coffee.aromatics.distinct(), coffee.aromatics)
            coffee.aromatics.forEach { tag ->
                assertTrue(coffee.id, tag.isNotBlank())
                assertEquals(coffee.id, tag.trim(), tag)
            }
        }
    }

    @Test
    fun `most coffees have a bundled image`() {
        val withImage = GeneratedCatalogue.coffees.count { it.imageRes != null }

        assertTrue(withImage >= GeneratedCatalogue.coffees.size / 2)
    }
}
