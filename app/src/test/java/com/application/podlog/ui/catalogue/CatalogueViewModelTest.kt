package com.application.podlog.ui.catalogue

import com.application.podlog.data.catalogue.CatalogueCoffee
import com.application.podlog.data.catalogue.CatalogueRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogueViewModelTest {
    @Test
    fun `initial state exposes repository coffees`() {
        val coffees =
            listOf(
                CatalogueCoffee(id = "ristretto", name = "Ristretto"),
                CatalogueCoffee(id = "volluto", name = "Volluto"),
            )

        val viewModel = CatalogueViewModel(CatalogueRepository { coffees })

        assertEquals(coffees, viewModel.uiState.value.coffees)
    }

    @Test
    fun `empty repository produces empty state`() {
        val viewModel = CatalogueViewModel(CatalogueRepository { emptyList() })

        assertTrue(viewModel.uiState.value.coffees.isEmpty())
    }
}
