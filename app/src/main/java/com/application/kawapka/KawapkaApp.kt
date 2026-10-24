package com.application.kawapka

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.application.kawapka.data.catalogue.CatalogueRepository
import com.application.kawapka.data.collection.CollectionRepository
import com.application.kawapka.ui.home.HomeRoute
import com.application.kawapka.ui.theme.KawapkaTheme

@Composable
fun KawapkaApp(
    catalogueRepository: CatalogueRepository,
    collectionRepository: CollectionRepository,
    modifier: Modifier = Modifier,
) {
    KawapkaTheme {
        HomeRoute(
            catalogueRepository = catalogueRepository,
            collectionRepository = collectionRepository,
            modifier = modifier,
        )
    }
}
