package com.application.kawapka.ui.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.kawapka.R
import com.application.kawapka.data.catalogue.DummyCatalogue
import com.application.kawapka.ui.catalogue.CoffeeCard
import com.application.kawapka.ui.components.CoffeeSearchField
import com.application.kawapka.ui.components.CoffeeSearchNoResultsState
import com.application.kawapka.ui.theme.KawapkaTheme

@Composable
fun CollectionScreen(
    items: List<CollectionItemUiState>,
    query: String,
    onQueryChange: (String) -> Unit,
    onRemove: (String) -> Unit,
    onBrowseCatalogue: () -> Unit,
    onSetRating: (String, Float) -> Unit,
    onClearRating: (String) -> Unit,
    sort: CollectionSort,
    modifier: Modifier = Modifier,
) {
    var ratingCoffeeId by rememberSaveable { mutableStateOf<String?>(null) }
    val ratingItem = items.firstOrNull { item -> item.coffee.id == ratingCoffeeId }

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
                CollectionGrid(
                    items = items,
                    query = query,
                    onRemove = onRemove,
                    onRateClick = { coffeeId -> ratingCoffeeId = coffeeId },
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
                CollectionEmptyState(
                    onBrowseCatalogue = onBrowseCatalogue,
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                )
        }
    }

    if (ratingItem != null) {
        RatingSheet(
            item = ratingItem,
            onSetRating = { rating -> onSetRating(ratingItem.coffee.id, rating) },
            onClearRating = { onClearRating(ratingItem.coffee.id) },
            onDismiss = { ratingCoffeeId = null },
        )
    }
}

@Composable
private fun CollectionGrid(
    items: List<CollectionItemUiState>,
    query: String,
    onRemove: (String) -> Unit,
    onRateClick: (String) -> Unit,
    sort: CollectionSort,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()
    // A new result set starts at its first item; otherwise clearing a search
    // keeps the previous result's position deep in the full list.
    LaunchedEffect(sort, query) { gridState.scrollToItem(0) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = items, key = { item -> item.coffee.id }) { item ->
            CoffeeCard(
                coffee = item.coffee,
                modifier = Modifier.fillMaxWidth(),
            ) {
                CollectionActions(
                    item = item,
                    onRemove = onRemove,
                    onRateClick = onRateClick,
                )
            }
        }
    }
}

@Composable
private fun CollectionActions(
    item: CollectionItemUiState,
    onRemove: (String) -> Unit,
    onRateClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RatingButton(
            rating = item.rating,
            onClick = { onRateClick(item.coffee.id) },
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(4.dp))
        RemoveButton(
            coffeeId = item.coffee.id,
            onRemove = onRemove,
        )
    }
}

@Composable
private fun RatingButton(
    rating: Float?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description =
        if (rating == null) {
            stringResource(R.string.collection_rating_button_unrated)
        } else {
            stringResource(R.string.collection_rating_button_rated, formatRating(rating))
        }
    OutlinedButton(
        onClick = onClick,
        modifier =
            modifier
                .height(36.dp)
                .semantics { contentDescription = description },
        border = BorderStroke(1.dp, if (rating == null) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.secondary),
        colors =
            ButtonDefaults.outlinedButtonColors(
                contentColor = if (rating == null) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.secondary,
            ),
        contentPadding = PaddingValues(horizontal = 8.dp),
    ) {
        Icon(
            imageVector = if (rating == null) Icons.Outlined.Star else Icons.Filled.Star,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text =
                if (rating == null) {
                    stringResource(R.string.collection_rate)
                } else {
                    stringResource(R.string.collection_rating_value, formatRating(rating))
                },
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
        )
    }
}

@Composable
private fun RemoveButton(
    coffeeId: String,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable(role = Role.Button) { onRemove(coffeeId) },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.collection_remove),
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
private fun CollectionEmptyState(
    onBrowseCatalogue: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            text = stringResource(R.string.collection_empty_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.collection_empty_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBrowseCatalogue) {
            Text(text = stringResource(R.string.collection_browse_catalogue))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionScreenPreview() {
    KawapkaTheme {
        CollectionScreen(
            items = sortCollection(previewCollectionItems(), CollectionSort.Name),
            query = "",
            onQueryChange = {},
            onRemove = {},
            onBrowseCatalogue = {},
            onSetRating = { _, _ -> },
            onClearRating = {},
            sort = CollectionSort.Name,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionScreenFilteredPreview() {
    val items = previewCollectionItems()
    KawapkaTheme {
        CollectionScreen(
            items = sortCollection(filterCollection(items, "Ispirazione"), CollectionSort.Name),
            query = "Ispirazione",
            onQueryChange = {},
            onRemove = {},
            onBrowseCatalogue = {},
            onSetRating = { _, _ -> },
            onClearRating = {},
            sort = CollectionSort.Name,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionScreenRatingSortedPreview() {
    KawapkaTheme {
        CollectionScreen(
            items = sortCollection(previewCollectionItems(), CollectionSort.RatingDescending),
            query = "",
            onQueryChange = {},
            onRemove = {},
            onBrowseCatalogue = {},
            onSetRating = { _, _ -> },
            onClearRating = {},
            sort = CollectionSort.RatingDescending,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionScreenEmptyPreview() {
    KawapkaTheme {
        CollectionScreen(
            items = emptyList(),
            query = "",
            onQueryChange = {},
            onRemove = {},
            onBrowseCatalogue = {},
            onSetRating = { _, _ -> },
            onClearRating = {},
            sort = CollectionSort.Name,
        )
    }
}

private fun previewCollectionItems(): List<CollectionItemUiState> {
    val coffees =
        DummyCatalogue.coffees.take(3) +
            DummyCatalogue.coffees.first { coffee -> coffee.intensity == null }
    val ratings = listOf<Float?>(4.5f, 3f, null, 5f)
    return coffees.zip(ratings) { coffee, rating -> CollectionItemUiState(coffee = coffee, rating = rating) }
}
