package com.application.kawapka.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.application.kawapka.R
import com.application.kawapka.data.catalogue.DummyCatalogue
import com.application.kawapka.ui.catalogue.CatalogueItemUiState
import com.application.kawapka.ui.catalogue.CatalogueScreen
import com.application.kawapka.ui.collection.CollectionScreen
import com.application.kawapka.ui.theme.KawapkaTheme

enum class HomeTab(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    Collection(R.string.nav_collection, Icons.Default.Home),
    Catalogue(R.string.nav_catalogue, Icons.AutoMirrored.Filled.List),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    selectedTab: HomeTab,
    uiState: HomeUiState,
    onTabSelected: (HomeTab) -> Unit,
    onAddToCollection: (String) -> Unit,
    onRemoveFromCollection: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(selectedTab.labelRes)) })
        },
        bottomBar = {
            NavigationBar {
                HomeTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { onTabSelected(tab) },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = null,
                            )
                        },
                        label = { Text(text = stringResource(tab.labelRes)) },
                    )
                }
            }
        },
    ) { innerPadding ->
        val contentModifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        when (selectedTab) {
            HomeTab.Collection ->
                CollectionScreen(
                    coffees = uiState.collection,
                    onRemove = onRemoveFromCollection,
                    onBrowseCatalogue = { onTabSelected(HomeTab.Catalogue) },
                    modifier = contentModifier,
                )

            HomeTab.Catalogue ->
                CatalogueScreen(
                    items = uiState.catalogue,
                    onAdd = onAddToCollection,
                    modifier = contentModifier,
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenCataloguePreview() {
    KawapkaTheme {
        HomeScreen(
            selectedTab = HomeTab.Catalogue,
            uiState = previewState(),
            onTabSelected = {},
            onAddToCollection = {},
            onRemoveFromCollection = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenCollectionPreview() {
    KawapkaTheme {
        HomeScreen(
            selectedTab = HomeTab.Collection,
            uiState = previewState(),
            onTabSelected = {},
            onAddToCollection = {},
            onRemoveFromCollection = {},
        )
    }
}

private fun previewState(): HomeUiState {
    val coffees = DummyCatalogue.coffees.take(4)
    return HomeUiState(
        catalogue = coffees.map { coffee -> CatalogueItemUiState(coffee = coffee, isInCollection = true) },
        collection = coffees,
    )
}
