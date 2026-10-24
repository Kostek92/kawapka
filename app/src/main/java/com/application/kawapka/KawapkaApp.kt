package com.application.kawapka

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.application.kawapka.data.catalogue.GeneratedCatalogueRepository
import com.application.kawapka.ui.catalogue.CatalogueRoute
import com.application.kawapka.ui.theme.KawapkaTheme

@Composable
fun KawapkaApp(modifier: Modifier = Modifier) {
    val catalogueRepository = remember { GeneratedCatalogueRepository() }
    KawapkaTheme {
        CatalogueRoute(
            repository = catalogueRepository,
            modifier = modifier,
        )
    }
}
