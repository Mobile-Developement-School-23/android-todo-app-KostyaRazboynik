package com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.theme.AppTheme
import com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.AddFragmentComposeAction
import java.text.DateFormat
import java.util.Date

@Composable
fun TodoEditorDeadlineField(
    deadline: Long?,
    onAction: (AddFragmentComposeAction) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(72.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var openDialog by remember { mutableStateOf(false) }

        DatePicker(
            date = deadline,
            open = openDialog,
            onDialogClose = { openDialog = false },
            onAction = onAction
        )

        Column {
            Text(
                text = stringResource(id = R.string.do_until),
                style = AppTheme.typography.body,
                color = AppTheme.colors.labelPrimary
            )
            AnimatedVisibility(deadline != null) {
                val dateText =
                    remember(deadline) {
                        deadline?.let { DateFormat.getDateInstance(DateFormat.DEFAULT).format(it) }
                    }
                if (dateText != null) {
                    Text(
                        modifier = Modifier.clickable { openDialog = true },
                        text = dateText,
                        style = AppTheme.typography.subhead,
                        color = AppTheme.colors.colorBlue
                    )
                }
            }
        }
        Switch(
            checked = deadline != null,
            onCheckedChange = {
                if (it) openDialog = true
                else onAction(AddFragmentComposeAction.UpdateDeadline(null))
            },
            colors = androidx.compose.material.SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.colorBlue,
                checkedTrackColor = AppTheme.colors.colorBlue,
                uncheckedThumbColor = AppTheme.colors.backElevated,
                uncheckedTrackColor = AppTheme.colors.supportOverlay
            )
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    date: Long?,
    open: Boolean,
    onDialogClose: () -> Unit,
    onAction: (AddFragmentComposeAction) -> Unit,
) {
    if (!open) return

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date)
    val saveButtonEnabled by remember(datePickerState.selectedDateMillis) {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }

    DatePickerDialog(
        onDismissRequest = onDialogClose,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onAction(AddFragmentComposeAction.UpdateDeadline(Date(it)))
                    }
                    onDialogClose()
                },
                enabled = saveButtonEnabled
            ) {
                Text(
                    stringResource(R.string.ok).uppercase(),
                    style = AppTheme.typography.button
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDialogClose()
                }
            ) {
                Text(
                    stringResource(R.string.cancel).uppercase(),
                    style = AppTheme.typography.button
                )
            }
        },
        colors = DatePickerDefaults.colors(

        )
    ) {
        androidx.compose.material3.DatePicker(state = datePickerState)
    }
}

@Preview
@Composable
fun PreviewTodoEditorDeadlineFieldOn() {
    AppTheme {
        TodoEditorDeadlineField(74343278) {}
    }
}

@Preview
@Composable
fun PreviewTodoEditorDeadlineFieldOff() {
    AppTheme {
        TodoEditorDeadlineField(null) {}
    }
}
