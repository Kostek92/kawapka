package com.application.kawapka.data.catalogue

/**
 * Catalogue repository backed by the committed generated snapshot.
 *
 * Re-run `scripts/generate-catalogue` to refresh [GeneratedCatalogue]; this
 * class and its callers stay unchanged.
 */
class GeneratedCatalogueRepository : CatalogueRepository {
    override fun coffees(): List<CatalogueCoffee> = GeneratedCatalogue.coffees
}
