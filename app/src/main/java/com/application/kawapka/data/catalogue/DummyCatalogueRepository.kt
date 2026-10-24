package com.application.kawapka.data.catalogue

class DummyCatalogueRepository : CatalogueRepository {
    override fun coffees(): List<CatalogueCoffee> = DummyCatalogue.coffees
}
