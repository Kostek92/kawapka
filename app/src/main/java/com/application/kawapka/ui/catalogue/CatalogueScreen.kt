package com.application.kawapka.ui.catalogue

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.kawapka.R
import com.application.kawapka.data.catalogue.DummyCatalogue
import com.application.kawapka.ui.components.CoffeeSearchField
import com.application.kawapka.ui.components.CoffeeSearchNoResultsState
import com.application.kawapka.ui.theme.KawapkaTheme

@Composable
fun CatalogueScreen(
    items: List<CatalogueItemUiState>,
    query: String,
    onQueryChange: (String) -> Unit,
    onAdd: (String) -> Unit,
    sort: CatalogueSort,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        CoffeeSearchField(
            query = query,
            onQueryChange = onQueryChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp),
        )
        when {
            items.isNotEmpty() ->
                CatalogueGrid(
                    items = items,
                    onAdd = onAdd,
                    sort = sort,
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                )

            query.isNotBlank() ->
                CoffeeSearchNoResultsState(
                    query = query,
                    onClearQuery = { onQueryChange("") },
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                )

            else ->
                CatalogueEmptyState(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                )
        }
    }
}

@Composable
private fun CatalogueGrid(
    items: List<CatalogueItemUiState>,
    onAdd: (String) -> Unit,
    sort: CatalogueSort,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()
    LaunchedEffect(sort) { gridState.scrollToItem(0) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = items, key = { item -> item.coffee.id }) { item ->
            CatalogueCard(
                item = item,
                onAdd = onAdd,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun CatalogueCard(
    item: CatalogueItemUiState,
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    CoffeeCard(
        coffee = item.coffee,
        modifier = modifier,
    ) {
        if (item.isInCollection) {
            AddedButton()
        } else {
            AddButton(
                coffeeId = item.coffee.id,
                onAdd = onAdd,
            )
        }
    }
}

@Composable
private fun AddButton(
    coffeeId: String,
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = { onAdd(coffeeId) },
        modifier =
            modifier
                .fillMaxWidth()
                .height(36.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.secondary),
        contentPadding = PaddingValues(horizontal = 12.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.catalogue_add),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

/** Non-interactive indicator; removal happens from the personal collection. */
@Composable
private fun AddedButton(modifier: Modifier = Modifier) {
    FilledTonalButton(
        onClick = {},
        enabled = false,
        modifier =
            modifier
                .fillMaxWidth()
                .height(36.dp),
        colors =
            ButtonDefaults.filledTonalButtonColors(
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                disabledContentColor = MaterialTheme.colorScheme.onSurface,
            ),
        contentPadding = PaddingValues(horizontal = 12.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.catalogue_added),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
private fun CatalogueEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.capsule_placeholder),
            contentDescription = null,
            modifier = Modifier.size(96.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.catalogue_empty),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogueScreenPreview() {
    KawapkaTheme {
        CatalogueScreen(
            items = previewCatalogueItems(),
            query = "",
            onQueryChange = {},
            onAdd = {},
            sort = CatalogueSort.Name,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogueScreenFilteredPreview() {
    val items = previewCatalogueItems()
    KawapkaTheme {
        CatalogueScreen(
            items = filterCatalogue(items, "Master"),
            query = "Master",
            onQueryChange = {},
            onAdd = {},
            sort = CatalogueSort.Name,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogueScreenNoResultsPreview() {
    val query = "Zulu"
    KawapkaTheme {
        CatalogueScreen(
            items = filterCatalogue(previewCatalogueItems(), query),
            query = query,
            onQueryChange = {},
            onAdd = {},
            sort = CatalogueSort.Name,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogueScreenEmptyPreview() {
    KawapkaTheme {
        CatalogueScreen(
            items = emptyList(),
            query = "",
            onQueryChange = {},
            onAdd = {},
            sort = CatalogueSort.Name,
        )
    }
}

private fun previewCatalogueItems(): List<CatalogueItemUiState> =
    DummyCatalogue.coffees.mapIndexed { index, coffee ->
        CatalogueItemUiState(coffee = coffee, isInCollection = index % 3 == 0)
    }
