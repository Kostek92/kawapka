package com.application.kawapka.ui.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.kawapka.R
import com.application.kawapka.data.catalogue.DummyCatalogue
import com.application.kawapka.ui.theme.KawapkaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingSheet(
    item: CollectionItemUiState,
    onSetRating: (Int) -> Unit,
    onClearRating: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        RatingSheetContent(
            coffeeName = item.coffee.name,
            rating = item.rating,
            onSetRating = onSetRating,
            onClearRating = onClearRating,
        )
    }
}

@Composable
private fun RatingSheetContent(
    coffeeName: String,
    rating: Int?,
    onSetRating: (Int) -> Unit,
    onClearRating: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.collection_rating_sheet_title, coffeeName),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.collection_rating_sheet_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (star in 1..5) {
                val selected = rating != null && star <= rating
                IconButton(
                    onClick = { onSetRating(star) },
                    modifier = Modifier.size(48.dp),
                ) {
                    Icon(
                        imageVector = if (selected) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = stringResource(R.string.collection_rating_star, star),
                        modifier = Modifier.size(32.dp),
                        tint = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text =
                if (rating == null) {
                    stringResource(R.string.collection_rating_unrated)
                } else {
                    stringResource(R.string.collection_rating_rated, rating)
                },
            style = MaterialTheme.typography.bodyMedium,
            color = if (rating == null) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.height(40.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (rating != null) {
                TextButton(onClick = onClearRating) {
                    Text(text = stringResource(R.string.collection_clear_rating))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingSheetContentPreview() {
    KawapkaTheme {
        RatingSheetContent(
            coffeeName = DummyCatalogue.coffees.first().name,
            rating = 4,
            onSetRating = {},
            onClearRating = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingSheetContentUnratedPreview() {
    KawapkaTheme {
        RatingSheetContent(
            coffeeName = DummyCatalogue.coffees.first().name,
            rating = null,
            onSetRating = {},
            onClearRating = {},
        )
    }
}
