package com.application.kawapka.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.application.kawapka.data.catalogue.CatalogueRepository
import com.application.kawapka.data.collection.CollectionRepository
import com.application.kawapka.ui.catalogue.CatalogueScreen

@Composable
fun HomeRoute(
    catalogueRepository: CatalogueRepository,
    collectionRepository: CollectionRepository,
    modifier: Modifier = Modifier,
) {
    val viewModel: HomeViewModel =
        viewModel(
            factory =
                viewModelFactory {
                    initializer { HomeViewModel(catalogueRepository, collectionRepository) }
                },
        )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CatalogueScreen(
        items = uiState.catalogue,
        onAdd = viewModel::addToCollection,
        modifier = modifier,
    )
}
