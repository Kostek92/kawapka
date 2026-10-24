package com.application.kawapka.ui.catalogue

import com.application.kawapka.data.catalogue.CatalogueCoffee

/** A catalogue coffee together with its personal collection membership. */
data class CatalogueItemUiState(
    val coffee: CatalogueCoffee,
    val isInCollection: Boolean,
)
