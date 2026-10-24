package com.application.kawapka.ui.collection

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.kawapka.R
import com.application.kawapka.data.catalogue.CatalogueCoffee
import com.application.kawapka.data.catalogue.DummyCatalogue
import com.application.kawapka.ui.catalogue.CoffeeCard
import com.application.kawapka.ui.theme.KawapkaTheme

@Composable
fun CollectionScreen(
    coffees: List<CatalogueCoffee>,
    onRemove: (String) -> Unit,
    onBrowseCatalogue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (coffees.isEmpty()) {
        CollectionEmptyState(
            onBrowseCatalogue = onBrowseCatalogue,
            modifier = modifier.fillMaxSize(),
        )
    } else {
        CollectionGrid(
            coffees = coffees,
            onRemove = onRemove,
            modifier = modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun CollectionGrid(
    coffees: List<CatalogueCoffee>,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = coffees, key = { coffee -> coffee.id }) { coffee ->
            CoffeeCard(
                coffee = coffee,
                modifier = Modifier.fillMaxWidth(),
            ) {
                RemoveButton(
                    coffeeId = coffee.id,
                    onRemove = onRemove,
                )
            }
        }
    }
}

@Composable
private fun RemoveButton(
    coffeeId: String,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = { onRemove(coffeeId) },
        modifier =
            modifier
                .fillMaxWidth()
                .height(36.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
        contentPadding = PaddingValues(horizontal = 12.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.collection_remove),
            style = MaterialTheme.typography.labelMedium,
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
            coffees = DummyCatalogue.coffees.take(4),
            onRemove = {},
            onBrowseCatalogue = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionScreenEmptyPreview() {
    KawapkaTheme {
        CollectionScreen(
            coffees = emptyList(),
            onRemove = {},
            onBrowseCatalogue = {},
        )
    }
}
