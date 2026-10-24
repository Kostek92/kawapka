package com.application.kawapka.ui.catalogue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.application.kawapka.data.catalogue.CatalogueRepository

@Composable
fun CatalogueRoute(
    repository: CatalogueRepository,
    modifier: Modifier = Modifier,
) {
    val viewModel: CatalogueViewModel =
        viewModel(
            factory =
                viewModelFactory {
                    initializer { CatalogueViewModel(repository) }
                },
        )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CatalogueScreen(
        coffees = uiState.coffees,
        modifier = modifier,
    )
}
