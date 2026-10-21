package com.application.podlog.ui.catalogue

import androidx.lifecycle.ViewModel
import com.application.podlog.data.catalogue.CatalogueCoffee
import com.application.podlog.data.catalogue.CatalogueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CatalogueUiState(
    val coffees: List<CatalogueCoffee> = emptyList(),
)

class CatalogueViewModel(
    repository: CatalogueRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CatalogueUiState(coffees = repository.coffees()))
    val uiState: StateFlow<CatalogueUiState> = _uiState.asStateFlow()
}
