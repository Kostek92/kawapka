package com.application.kawapka.ui.collection

/**
 * Supported orderings for the personal collection.
 *
 * Missing data always sorts last: unrated coffees after rated coffees and
 * coffees without an intensity after those with a value. [Name] breaks ties by
 * catalogue id; the other orders break ties by case-insensitive name and then
 * id.
 */
enum class CollectionSort {
    Name,
    IntensityDescending,
    RatingDescending,
}

/**
 * Returns [items] ordered by [sort].
 *
 * The order is deterministic: equal values fall back to the case-insensitive
 * coffee name and then the catalogue id, so the result does not depend on the
 * input order. A missing intensity or rating is placed after every present
 * value.
 */
internal fun sortCollection(
    items: List<CollectionItemUiState>,
    sort: CollectionSort,
): List<CollectionItemUiState> {
    val byNameThenId =
        compareBy<CollectionItemUiState, String>(String.CASE_INSENSITIVE_ORDER) { item -> item.coffee.name }
            .thenBy { item -> item.coffee.id }
    return when (sort) {
        CollectionSort.Name -> items.sortedWith(byNameThenId)
        CollectionSort.IntensityDescending ->
            items.sortedWith(
                compareBy<CollectionItemUiState> { item -> item.coffee.intensity == null }
                    .thenByDescending { item -> item.coffee.intensity ?: Int.MIN_VALUE }
                    .then(byNameThenId),
            )
        CollectionSort.RatingDescending ->
            items.sortedWith(
                compareBy<CollectionItemUiState> { item -> item.rating == null }
                    .thenByDescending { item -> item.rating ?: 0f }
                    .then(byNameThenId),
            )
    }
}
