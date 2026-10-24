package com.application.kawapka.data.catalogue

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GeneratedCatalogueRepositoryTest {
    @Test
    fun `repository exposes the generated snapshot`() {
        val coffees = GeneratedCatalogueRepository().coffees()

        assertEquals(GeneratedCatalogue.coffees, coffees)
        assertTrue(coffees.isNotEmpty())
    }
}
