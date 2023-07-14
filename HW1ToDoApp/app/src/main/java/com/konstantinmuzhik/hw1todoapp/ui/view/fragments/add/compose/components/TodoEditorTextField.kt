package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose.theme.AppTheme
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose.AddFragmentComposeAction

@Composable
fun TodoEditorTextField(
    text: String = "",
    onAction: (AddFragmentComposeAction) -> Unit
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 8.dp)
            .padding(bottom = 12.dp)
            .padding(horizontal = 16.dp),
        value = text,
        onValueChange = { onAction(AddFragmentComposeAction.UpdateText(it)) },
        textStyle = AppTheme.typography.body.copy(
            color = AppTheme.colors.labelPrimary
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        minLines = 4,
        cursorBrush = SolidColor(AppTheme.colors.labelPrimary)
    ) { innerTextField ->
        Card(
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colors.backSecondary,
                contentColor = AppTheme.colors.labelTertiary,
            )
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                if (text.isEmpty()) {
                    Text(text = stringResource(id = R.string.task_title))
                }
                innerTextField.invoke()
            }
        }
    }
}

@Preview
@Composable
fun PreviewTodoEditorTextField() {
    AppTheme {
        TodoEditorTextField(
            text = "",
            onAction = {})
    }
}
