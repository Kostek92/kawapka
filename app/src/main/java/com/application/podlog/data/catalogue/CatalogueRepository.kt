package com.application.podlog.data.catalogue

/**
 * Source of the shared coffee catalogue.
 *
 * Production uses the committed snapshot via [GeneratedCatalogueRepository];
 * [DummyCatalogueRepository] remains available for previews and tests. Screen
 * code should only depend on this interface.
 */
fun interface CatalogueRepository {
    fun coffees(): List<CatalogueCoffee>
}
