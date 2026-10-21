package com.application.podlog.data.catalogue

class DummyCatalogueRepository : CatalogueRepository {
    override fun coffees(): List<CatalogueCoffee> = DummyCatalogue.coffees
}
