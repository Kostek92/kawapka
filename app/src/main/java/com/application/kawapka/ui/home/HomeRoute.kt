package com.application.kawapka.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.application.kawapka.data.catalogue.CatalogueRepository
import com.application.kawapka.data.collection.CollectionRepository

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
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.Catalogue) }

    HomeScreen(
        selectedTab = selectedTab,
        uiState = uiState,
        onTabSelected = { tab -> selectedTab = tab },
        onCatalogueQueryChange = viewModel::setCatalogueQuery,
        onAddToCollection = viewModel::addToCollection,
        onRemoveFromCollection = viewModel::removeFromCollection,
        onSetRating = viewModel::setRating,
        onClearRating = viewModel::clearRating,
        modifier = modifier,
    )
}
