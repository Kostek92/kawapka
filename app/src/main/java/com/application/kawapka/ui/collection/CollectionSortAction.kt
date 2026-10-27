package com.application.kawapka.ui.collection

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.kawapka.R
import com.application.kawapka.ui.theme.KawapkaTheme

/** Top-bar action that shows and changes the collection sort. */
@Composable
fun CollectionSortAction(
    sort: CollectionSort,
    onSortChange: (CollectionSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val sortLabel = stringResource(sort.labelRes())

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(R.drawable.ic_sort),
                contentDescription = stringResource(R.string.collection_sort_action, sortLabel),
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            CollectionSort.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(option.labelRes())) },
                    onClick = {
                        onSortChange(option)
                        expanded = false
                    },
                    leadingIcon = {
                        if (option == sort) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                            )
                        } else {
                            Spacer(modifier = Modifier.size(24.dp))
                        }
                    },
                    modifier = Modifier.semantics { selected = option == sort },
                )
            }
        }
    }
}

@StringRes
private fun CollectionSort.labelRes(): Int =
    when (this) {
        CollectionSort.Name -> R.string.collection_sort_name
        CollectionSort.IntensityDescending -> R.string.collection_sort_intensity
        CollectionSort.RatingDescending -> R.string.collection_sort_rating
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
