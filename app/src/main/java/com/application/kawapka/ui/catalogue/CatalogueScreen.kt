package com.application.kawapka.ui.catalogue

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.kawapka.R
import com.application.kawapka.data.catalogue.CatalogueCoffee
import com.application.kawapka.data.catalogue.DummyCatalogue
import com.application.kawapka.ui.theme.KawapkaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogueScreen(
    coffees: List<CatalogueCoffee>,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.catalogue_title)) })
        },
    ) { innerPadding ->
        if (coffees.isEmpty()) {
            CatalogueEmptyState(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
            )
        } else {
            CatalogueGrid(
                coffees = coffees,
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun CatalogueGrid(
    coffees: List<CatalogueCoffee>,
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
            CatalogueCard(
                coffee = coffee,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun CatalogueCard(
    coffee: CatalogueCoffee,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.25f),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(coffee.imageRes ?: R.drawable.capsule_placeholder),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                    contentScale = ContentScale.Fit,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = coffee.name,
                style = MaterialTheme.typography.titleMedium,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Reserve lines even without tag data so every card aligns.
            val headline = coffee.headline?.takeIf { it.isNotBlank() } ?: "\u00A0"
            Text(
                text = headline,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(2.dp))
            val intensity = coffee.intensity
            val intensityText =
                if (intensity == null) {
                    "\u00A0"
                } else {
                    stringResource(
                        R.string.catalogue_intensity,
                        intensity,
                        CatalogueCoffee.MAX_INTENSITY,
                    )
                }
            Text(
                text = intensityText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                minLines = 1,
                maxLines = 1,
            )
        }
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
        CatalogueScreen(coffees = DummyCatalogue.coffees)
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogueScreenEmptyPreview() {
    KawapkaTheme {
        CatalogueScreen(coffees = emptyList())
    }
}
