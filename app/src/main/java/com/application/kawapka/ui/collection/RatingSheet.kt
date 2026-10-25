package com.application.kawapka.ui.collection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
    onSetRating: (Float) -> Unit,
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
    rating: Float?,
    onSetRating: (Float) -> Unit,
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
        RatingStars(rating = rating, onSetRating = onSetRating)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text =
                if (rating == null) {
                    stringResource(R.string.collection_rating_unrated)
                } else {
                    stringResource(R.string.collection_rating_rated, formatRating(rating))
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

@Composable
private fun RatingStars(
    rating: Float?,
    onSetRating: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        STAR_TARGETS.forEach { target ->
            Box(modifier = Modifier.size(48.dp)) {
                StarVisual(
                    fill = starFill(rating, target.star),
                    modifier = Modifier.align(Alignment.Center),
                )
                RatingTapTarget(
                    rating = target.half,
                    onClick = onSetRating,
                    modifier =
                        Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxHeight()
                            .fillMaxWidth(0.5f),
                )
                RatingTapTarget(
                    rating = target.whole,
                    onClick = onSetRating,
                    modifier =
                        Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .fillMaxWidth(0.5f),
                )
            }
        }
    }
}

@Composable
private fun RatingTapTarget(
    rating: Float,
    onClick: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(R.string.collection_rating_star, formatRating(rating))
    Box(
        modifier =
            modifier
                .clickable(role = Role.Button) { onClick(rating) }
                .semantics { contentDescription = description },
    )
}

@Composable
private fun StarVisual(
    fill: Float,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.size(40.dp)) {
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = MaterialTheme.colorScheme.outline,
        )
        if (fill > 0f) {
            // Clip the full-size star drawing; laying out a narrower icon would
            // scale it down instead of showing half of the same star.
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .drawWithContent {
                            clipRect(right = size.width * fill) {
                                this@drawWithContent.drawContent()
                            }
                        },
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

private data class StarTarget(
    val star: Int,
    val half: Float,
    val whole: Float,
)

private val STAR_TARGETS =
    listOf(
        StarTarget(star = 1, half = 0.5f, whole = 1f),
        StarTarget(star = 2, half = 1.5f, whole = 2f),
        StarTarget(star = 3, half = 2.5f, whole = 3f),
        StarTarget(star = 4, half = 3.5f, whole = 4f),
        StarTarget(star = 5, half = 4.5f, whole = 5f),
    )

@Preview(showBackground = true)
@Composable
private fun RatingSheetContentPreview() {
    KawapkaTheme {
        RatingSheetContent(
            coffeeName = DummyCatalogue.coffees.first().name,
            rating = 4.5f,
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
