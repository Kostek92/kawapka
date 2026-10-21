package com.application.podlog.data.catalogue

/**
 * Source of the shared coffee catalogue.
 *
 * Phase 2 will add an implementation backed by the generated catalogue
 * snapshot; screen code should only depend on this interface.
 */
fun interface CatalogueRepository {
    fun coffees(): List<CatalogueCoffee>
}
