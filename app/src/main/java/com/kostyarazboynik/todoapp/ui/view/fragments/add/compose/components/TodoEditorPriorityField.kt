package com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.data.models.Importance
import com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.theme.AppTheme
import com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.AddFragmentComposeAction

@Composable
fun TodoEditorPriorityField(
    importance: Importance,
    onAction: (AddFragmentComposeAction) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable { menuExpanded = true }
    ) {
        PriorityDropdownMenu(
            expanded = menuExpanded,
            closeMenu = { menuExpanded = false },
            onAction = onAction
        )

        Text(
            text = stringResource(id = R.string.priority),
            style = AppTheme.typography.body,
            color = AppTheme.colors.labelPrimary
        )
        Text(
            text = stringResource(
                id = when (importance) {
                    Importance.HIGH -> R.string.high_priority
                    Importance.LOW -> R.string.low_priority
                    else -> R.string.no_priority
                }
            ),
            style = AppTheme.typography.subhead,
            color = when (importance) {
                Importance.HIGH -> AppTheme.colors.colorRed
                Importance.LOW -> AppTheme.colors.colorGreen
                else -> AppTheme.colors.labelSecondary
            }
        )
    }
}

@Composable
fun PriorityDropdownMenu(
    expanded: Boolean,
    closeMenu: () -> Unit,
    onAction: (AddFragmentComposeAction) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = closeMenu,
        modifier = Modifier.background(
            color = AppTheme.colors.backElevated
        )
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.no_priority),
                    style = AppTheme.typography.body,
                )
            },
            onClick = {
                onAction(AddFragmentComposeAction.UpdatePriority(Importance.NO))
                closeMenu()
            },
            colors = MenuDefaults.itemColors(
                textColor = AppTheme.colors.labelPrimary
            )
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.high_priority),
                    style = AppTheme.typography.body,
                )
            },
            onClick = {
                onAction(AddFragmentComposeAction.UpdatePriority(Importance.HIGH))
                closeMenu()
            },
            colors = MenuDefaults.itemColors(
                textColor = AppTheme.colors.labelPrimary
            )
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.low_priority),
                    style = AppTheme.typography.body
                )
            },
            onClick = {
                onAction(AddFragmentComposeAction.UpdatePriority(Importance.LOW))
                closeMenu()
            },
            colors = MenuDefaults.itemColors(
                textColor = AppTheme.colors.labelPrimary
            )
        )

    }
}


@Preview
@Composable
fun PreviewTodoEditorPriorityField() {
    AppTheme {
        TodoEditorPriorityField(importance = Importance.NO) {}
    }
}
