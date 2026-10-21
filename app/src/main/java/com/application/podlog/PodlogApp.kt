package com.application.podlog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.application.podlog.data.catalogue.DummyCatalogueRepository
import com.application.podlog.ui.catalogue.CatalogueRoute
import com.application.podlog.ui.theme.PodlogTheme

@Composable
fun PodlogApp(modifier: Modifier = Modifier) {
    val catalogueRepository = remember { DummyCatalogueRepository() }
    PodlogTheme {
        CatalogueRoute(
            repository = catalogueRepository,
            modifier = modifier,
        )
    }
}
