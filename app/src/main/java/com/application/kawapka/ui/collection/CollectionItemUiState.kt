package com.application.kawapka.ui.collection

import com.application.kawapka.data.catalogue.CatalogueCoffee

/**
 * A collection coffee together with its personal rating.
 *
 * [rating] is a half-star value from 0.5 to 5, or `null` when the coffee is
 * unrated; it is never represented as zero stars.
 */
data class CollectionItemUiState(
    val coffee: CatalogueCoffee,
    val rating: Float?,
)
