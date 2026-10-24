package com.application.kawapka.ui.catalogue

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.application.kawapka.R
import com.application.kawapka.data.catalogue.CatalogueCoffee

/**
 * Shared card shell for a coffee in the catalogue or personal collection.
 *
 * The caller supplies the membership action shown below the coffee summary.
 */
@Composable
fun CoffeeCard(
    coffee: CatalogueCoffee,
    modifier: Modifier = Modifier,
    action: @Composable () -> Unit,
) {
    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            CoffeeSummary(coffee = coffee)
            Spacer(modifier = Modifier.height(8.dp))
            action()
        }
    }
}

@Composable
private fun CoffeeSummary(
    coffee: CatalogueCoffee,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
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
