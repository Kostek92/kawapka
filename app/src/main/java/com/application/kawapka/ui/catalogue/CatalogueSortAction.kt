package com.application.kawapka.ui.catalogue

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.application.kawapka.R
import com.application.kawapka.ui.components.SortAction
import com.application.kawapka.ui.components.SortOption
import com.application.kawapka.ui.theme.KawapkaTheme

/** Top-bar action that shows and changes the catalogue sort. */
@Composable
fun CatalogueSortAction(
    sort: CatalogueSort,
    onSortChange: (CatalogueSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sortLabel = stringResource(sort.labelRes())
    SortAction(
        selected = sort,
        options = CatalogueSort.entries.map { option -> SortOption(option, option.labelRes()) },
        contentDescription = stringResource(R.string.sort_catalogue_action, sortLabel),
        onSelect = onSortChange,
        modifier = modifier,
    )
}

private fun CatalogueSort.labelRes(): Int =
    when (this) {
        CatalogueSort.Name -> R.string.sort_name
        CatalogueSort.IntensityDescending -> R.string.sort_intensity
    }

@Preview(showBackground = true)
@Composable
private fun CatalogueSortActionPreview() {
    KawapkaTheme {
        CatalogueSortAction(
            sort = CatalogueSort.IntensityDescending,
            onSortChange = {},
        )
    }
}
