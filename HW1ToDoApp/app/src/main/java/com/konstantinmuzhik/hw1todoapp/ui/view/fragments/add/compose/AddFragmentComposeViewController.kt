package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose.theme.AppTheme
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose.components.TodoEditorDeadlineField
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose.components.TodoEditorDivider
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose.components.TodoEditorPriorityField
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose.components.TodoEditorTextField
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose.components.TodoEditorToolbar

@Composable
fun AddFragmentComposeViewController(
    todoItem: ToDoItem,
    onAction: (AddFragmentComposeAction) -> Unit
) {

    val text = todoItem.title
    val priority = todoItem.priority
    val deadline = todoItem.deadline


    Scaffold(
        topBar = { TodoEditorToolbar(text = text, onAction = onAction) },
        containerColor = AppTheme.colors.backPrimary
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                TodoEditorTextField(text = text, onAction = onAction)
                TodoEditorPriorityField(priority = priority, onAction = onAction)
                TodoEditorDivider(PaddingValues(horizontal = 16.dp))
                TodoEditorDeadlineField(deadline = deadline?.time, onAction = onAction)
                TodoEditorDivider(PaddingValues(top = 24.dp, bottom = 8.dp))
            }
        }
    }
}

@Preview
@Composable
fun LightPreviewTodoEditor() {
    AppTheme(darkTheme = false) {
        AddFragmentComposeViewController(ToDoItem(), onAction = {})
    }
}

@Preview
@Composable
fun DarkPreviewTodoEditor() {
    AppTheme(darkTheme = true) {
        AddFragmentComposeViewController(ToDoItem(),onAction = {})
    }
}
