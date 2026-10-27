package com.application.kawapka.ui.collection

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.application.kawapka.R
import com.application.kawapka.ui.components.SortAction
import com.application.kawapka.ui.components.SortOption
import com.application.kawapka.ui.theme.KawapkaTheme

/** Top-bar action that shows and changes the collection sort. */
@Composable
fun CollectionSortAction(
    sort: CollectionSort,
    onSortChange: (CollectionSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sortLabel = stringResource(sort.labelRes())
    SortAction(
        selected = sort,
        options = CollectionSort.entries.map { option -> SortOption(option, option.labelRes()) },
        contentDescription = stringResource(R.string.sort_collection_action, sortLabel),
        onSelect = onSortChange,
        modifier = modifier,
    )
}

private fun CollectionSort.labelRes(): Int =
    when (this) {
        CollectionSort.Name -> R.string.sort_name
        CollectionSort.IntensityDescending -> R.string.sort_intensity
        CollectionSort.RatingDescending -> R.string.sort_rating
    }

@Preview(showBackground = true)
@Composable
private fun CollectionSortActionPreview() {
    KawapkaTheme {
        CollectionSortAction(
            sort = CollectionSort.RatingDescending,
            onSortChange = {},
        )
    }
}
