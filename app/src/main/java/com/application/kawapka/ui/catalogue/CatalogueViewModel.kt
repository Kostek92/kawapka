package com.application.kawapka.ui.catalogue

import androidx.lifecycle.ViewModel
import com.application.kawapka.data.catalogue.CatalogueCoffee
import com.application.kawapka.data.catalogue.CatalogueRepository
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
