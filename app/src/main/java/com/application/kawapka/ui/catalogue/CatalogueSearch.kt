package com.application.kawapka.ui.catalogue

/**
 * Returns the catalogue items whose coffee name contains [query].
 *
 * Matching is case-insensitive and uses the coffee name only, never the
 * headline, aromatics, or description. The query is trimmed before matching;
 * a blank query returns [items] unchanged. Catalogue order is preserved.
 */
internal fun filterCatalogue(
    items: List<CatalogueItemUiState>,
    query: String,
): List<CatalogueItemUiState> {
    val trimmedQuery = query.trim()
    if (trimmedQuery.isEmpty()) return items
    return items.filter { item -> item.coffee.name.contains(trimmedQuery, ignoreCase = true) }
}
