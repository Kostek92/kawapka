package com.application.kawapka.ui.components

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
import androidx.compose.ui.unit.dp
import com.application.kawapka.R

/** One selectable sort option in a [SortAction] menu. */
data class SortOption<T>(
    val value: T,
    @StringRes val labelRes: Int,
)

/**
 * Shared icon-only top-bar sort action.
 *
 * The menu marks the selected option with a check icon and closes after a
 * selection. [contentDescription] should name the current sort so the action is
 * understandable without opening the menu.
 */
@Composable
fun <T> SortAction(
    selected: T,
    options: List<SortOption<T>>,
    contentDescription: String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(R.drawable.ic_sort),
                contentDescription = contentDescription,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(option.labelRes)) },
                    onClick = {
                        onSelect(option.value)
                        expanded = false
                    },
                    leadingIcon = {
                        if (option.value == selected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                            )
                        } else {
                            Spacer(modifier = Modifier.size(24.dp))
                        }
                    },
                    modifier = Modifier.semantics { this.selected = option.value == selected },
                )
            }
        }
    }
}
