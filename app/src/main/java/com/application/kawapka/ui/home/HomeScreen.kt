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
import com.application.kawapka.ui.catalogue.CatalogueSort
import com.application.kawapka.ui.catalogue.CatalogueSortAction
import com.application.kawapka.ui.catalogue.sortCatalogue
import com.application.kawapka.ui.collection.CollectionItemUiState
import com.application.kawapka.ui.collection.CollectionScreen
import com.application.kawapka.ui.collection.CollectionSort
import com.application.kawapka.ui.collection.CollectionSortAction
import com.application.kawapka.ui.collection.sortCollection
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
    onCatalogueQueryChange: (String) -> Unit,
    onCollectionQueryChange: (String) -> Unit,
    onCatalogueSortChange: (CatalogueSort) -> Unit,
    onCollectionSortChange: (CollectionSort) -> Unit,
    onAddToCollection: (String) -> Unit,
    onRemoveFromCollection: (String) -> Unit,
    onSetRating: (String, Float) -> Unit,
    onClearRating: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(selectedTab.labelRes)) },
                actions = {
                    when {
                        selectedTab == HomeTab.Collection && uiState.hasCollectionItems ->
                            CollectionSortAction(
                                sort = uiState.collectionSort,
                                onSortChange = onCollectionSortChange,
                            )

                        selectedTab == HomeTab.Catalogue && uiState.hasCatalogueItems ->
                            CatalogueSortAction(
                                sort = uiState.catalogueSort,
                                onSortChange = onCatalogueSortChange,
                            )
                    }
                },
            )
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
                    items = uiState.collection,
                    query = uiState.collectionQuery,
                    onQueryChange = onCollectionQueryChange,
                    onRemove = onRemoveFromCollection,
                    onBrowseCatalogue = { onTabSelected(HomeTab.Catalogue) },
                    onSetRating = onSetRating,
                    onClearRating = onClearRating,
                    sort = uiState.collectionSort,
                    modifier = contentModifier,
                )

            HomeTab.Catalogue ->
                CatalogueScreen(
                    items = uiState.catalogue,
                    query = uiState.catalogueQuery,
                    onQueryChange = onCatalogueQueryChange,
                    onAdd = onAddToCollection,
                    sort = uiState.catalogueSort,
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
            onCatalogueQueryChange = {},
            onCollectionQueryChange = {},
            onCatalogueSortChange = {},
            onCollectionSortChange = {},
            onAddToCollection = {},
            onRemoveFromCollection = {},
            onSetRating = { _, _ -> },
            onClearRating = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenCatalogueSortedPreview() {
    KawapkaTheme {
        HomeScreen(
            selectedTab = HomeTab.Catalogue,
            uiState = previewState(catalogueSort = CatalogueSort.IntensityDescending),
            onTabSelected = {},
            onCatalogueQueryChange = {},
            onCollectionQueryChange = {},
            onCatalogueSortChange = {},
            onCollectionSortChange = {},
            onAddToCollection = {},
            onRemoveFromCollection = {},
            onSetRating = { _, _ -> },
            onClearRating = {},
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
            onCatalogueQueryChange = {},
            onCollectionQueryChange = {},
            onCatalogueSortChange = {},
            onCollectionSortChange = {},
            onAddToCollection = {},
            onRemoveFromCollection = {},
            onSetRating = { _, _ -> },
            onClearRating = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenCollectionSortedPreview() {
    KawapkaTheme {
        HomeScreen(
            selectedTab = HomeTab.Collection,
            uiState = previewState(collectionSort = CollectionSort.RatingDescending),
            onTabSelected = {},
            onCatalogueQueryChange = {},
            onCollectionQueryChange = {},
            onCatalogueSortChange = {},
            onCollectionSortChange = {},
            onAddToCollection = {},
            onRemoveFromCollection = {},
            onSetRating = { _, _ -> },
            onClearRating = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenEmptyCollectionPreview() {
    KawapkaTheme {
        HomeScreen(
            selectedTab = HomeTab.Collection,
            uiState = HomeUiState(hasCatalogueItems = true, hasCollectionItems = false),
            onTabSelected = {},
            onCatalogueQueryChange = {},
            onCollectionQueryChange = {},
            onCatalogueSortChange = {},
            onCollectionSortChange = {},
            onAddToCollection = {},
            onRemoveFromCollection = {},
            onSetRating = { _, _ -> },
            onClearRating = {},
        )
    }
}

private fun previewState(
    catalogueSort: CatalogueSort = CatalogueSort.Name,
    collectionSort: CollectionSort = CollectionSort.Name,
): HomeUiState {
    val coffees =
        DummyCatalogue.coffees.take(3) +
            DummyCatalogue.coffees.first { coffee -> coffee.intensity == null }
    val ratings = listOf<Float?>(4.5f, 3f, null, 5f)
    val catalogueItems = coffees.map { coffee -> CatalogueItemUiState(coffee = coffee, isInCollection = true) }
    val collectionItems = coffees.zip(ratings) { coffee, rating -> CollectionItemUiState(coffee = coffee, rating = rating) }
    return HomeUiState(
        catalogue = sortCatalogue(catalogueItems, catalogueSort),
        collection = sortCollection(collectionItems, collectionSort),
        catalogueSort = catalogueSort,
        collectionSort = collectionSort,
        hasCatalogueItems = true,
        hasCollectionItems = true,
    )
}
