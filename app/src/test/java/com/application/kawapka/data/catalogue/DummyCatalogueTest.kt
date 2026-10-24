package com.application.kawapka.data.catalogue

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DummyCatalogueTest {
    @Test
    fun `ids are non-blank and unique`() {
        val ids = DummyCatalogue.coffees.map { it.id }

        assertTrue(ids.all { it.isNotBlank() })
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `every coffee has a name`() {
        assertTrue(DummyCatalogue.coffees.all { it.name.isNotBlank() })
    }

    @Test
    fun `fixture keeps optional fields optional`() {
        assertTrue(DummyCatalogue.coffees.any { it.imageRes == null })
        assertTrue(DummyCatalogue.coffees.any { it.shortDescription.isNullOrBlank() })
    }
}
