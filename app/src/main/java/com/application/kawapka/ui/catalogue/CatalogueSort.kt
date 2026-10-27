package com.application.kawapka.ui.catalogue

/**
 * Supported orderings for the catalogue.
 *
 * Missing intensity always sorts last. [Name] breaks ties by catalogue id;
 * [IntensityDescending] breaks ties by case-insensitive name and then id.
 */
enum class CatalogueSort {
    Name,
    IntensityDescending,
}

/**
 * Returns [items] ordered by [sort].
 *
 * The order is deterministic: equal values fall back to the case-insensitive
 * coffee name and then the catalogue id, so the result does not depend on the
 * input order. A missing intensity is placed after every present value.
 */
internal fun sortCatalogue(
    items: List<CatalogueItemUiState>,
    sort: CatalogueSort,
): List<CatalogueItemUiState> {
    val byNameThenId =
        compareBy<CatalogueItemUiState, String>(String.CASE_INSENSITIVE_ORDER) { item -> item.coffee.name }
            .thenBy { item -> item.coffee.id }
    return when (sort) {
        CatalogueSort.Name -> items.sortedWith(byNameThenId)
        CatalogueSort.IntensityDescending ->
            items.sortedWith(
                compareBy<CatalogueItemUiState> { item -> item.coffee.intensity == null }
                    .thenByDescending { item -> item.coffee.intensity ?: Int.MIN_VALUE }
                    .then(byNameThenId),
            )
    }
}
