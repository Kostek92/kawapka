package com.application.kawapka.ui.collection

/**
 * Returns the collection items whose coffee name contains [query].
 *
 * Matching is case-insensitive and uses the coffee name only. The query is
 * trimmed before matching; a blank query returns [items] unchanged. Collection
 * order is preserved.
 */
internal fun filterCollection(
    items: List<CollectionItemUiState>,
    query: String,
): List<CollectionItemUiState> {
    val trimmedQuery = query.trim()
    if (trimmedQuery.isEmpty()) return items
    return items.filter { item -> item.coffee.name.contains(trimmedQuery, ignoreCase = true) }
}
