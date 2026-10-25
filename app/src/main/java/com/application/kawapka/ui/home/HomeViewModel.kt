package com.application.kawapka.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.kawapka.data.catalogue.CatalogueCoffee
import com.application.kawapka.data.catalogue.CatalogueRepository
import com.application.kawapka.data.collection.CollectionEntry
import com.application.kawapka.data.collection.CollectionRepository
import com.application.kawapka.ui.catalogue.CatalogueItemUiState
import com.application.kawapka.ui.collection.CollectionItemUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val catalogue: List<CatalogueItemUiState> = emptyList(),
    val collection: List<CollectionItemUiState> = emptyList(),
)

class HomeViewModel(
    catalogueRepository: CatalogueRepository,
    private val collectionRepository: CollectionRepository,
) : ViewModel() {
    private val catalogue = catalogueRepository.coffees()

    val uiState: StateFlow<HomeUiState> =
        collectionRepository.entries
            .map { entries -> buildHomeUiState(catalogue, entries) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = buildHomeUiState(catalogue, emptyList()),
            )

    fun addToCollection(catalogueId: String) {
        viewModelScope.launch { collectionRepository.add(catalogueId) }
    }

    fun removeFromCollection(catalogueId: String) {
        viewModelScope.launch { collectionRepository.remove(catalogueId) }
    }

    fun setRating(
        catalogueId: String,
        rating: Float,
    ) {
        viewModelScope.launch { collectionRepository.setRating(catalogueId, rating) }
    }

    fun clearRating(catalogueId: String) {
        viewModelScope.launch { collectionRepository.setRating(catalogueId, null) }
    }
}

/**
 * Combines the shared catalogue with personal collection entries.
 *
 * Entries whose catalogue id is no longer in the snapshot stay in Room but are
 * not rendered; this keeps personal data safe across catalogue refreshes.
 */
internal fun buildHomeUiState(
    catalogue: List<CatalogueCoffee>,
    entries: List<CollectionEntry>,
): HomeUiState {
    val collectionIds = entries.mapTo(mutableSetOf()) { it.catalogueId }
    val coffeesById = catalogue.associateBy { it.id }
    val orderedEntries =
        entries
            .sortedWith(compareByDescending<CollectionEntry> { it.dateAdded }.thenBy { it.catalogueId })
            .distinctBy { it.catalogueId }

    return HomeUiState(
        catalogue =
            catalogue.map { coffee ->
                CatalogueItemUiState(
                    coffee = coffee,
                    isInCollection = coffee.id in collectionIds,
                )
            },
        collection =
            orderedEntries.mapNotNull { entry ->
                coffeesById[entry.catalogueId]?.let { coffee ->
                    CollectionItemUiState(
                        coffee = coffee,
                        rating = entry.rating,
                    )
                }
            },
    )
}
