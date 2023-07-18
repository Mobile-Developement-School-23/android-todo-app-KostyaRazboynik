package com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.theme.AppTheme
import com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.AddFragmentComposeAction


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoEditorToolbar(
    text: String,
    onAction: (AddFragmentComposeAction) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { onAction(AddFragmentComposeAction.Close) },
                enabled = true,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = AppTheme.colors.labelPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.save)
                )
            }
        },
        title = { },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.colors.taskToolBarColor,
            navigationIconContentColor = AppTheme.colors.labelPrimary
        ),
        actions = {
            TextButton(
                onClick = { onAction(AddFragmentComposeAction.Save) },
                enabled = text.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = AppTheme.colors.colorBlue,
                    containerColor = Color.Transparent,
                    disabledContentColor = AppTheme.colors.labelDisable,
                    disabledContainerColor = Color.Transparent,
                )
            ) {
                Text(
                    text = stringResource(id = R.string.create).uppercase(),
                    style = AppTheme.typography.button
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewTodoEditorToolbar() {
    AppTheme {
        TodoEditorToolbar(
            text = "",
            onAction = {}
        )
    }
}
