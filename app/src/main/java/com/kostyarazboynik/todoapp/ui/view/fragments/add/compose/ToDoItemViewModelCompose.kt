package com.kostyarazboynik.todoapp.ui.view.fragments.add.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kostyarazboynik.todoapp.data.models.Importance
import com.kostyarazboynik.todoapp.data.models.ToDoItem
import com.kostyarazboynik.todoapp.data.repository.ToDoItemsRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject


class ToDoItemViewModelCompose @Inject constructor(
    private val repository: ToDoItemsRepositoryImpl,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    var currentToDoItem by mutableStateOf(ToDoItem())
        private set

    fun setToDoItem() {
        currentToDoItem = ToDoItem().copy(id = UUID.randomUUID().toString())
    }

    fun addToDoItem() =
        coroutineScope.launch(Dispatchers.IO) {
            repository.createToDoItem(currentToDoItem.copy(changedAt = Date()).also {
                it.changedAt!!.time = System.currentTimeMillis()
                it.createdAt.time = System.currentTimeMillis()
            })
        }

    fun updateText(title: String) {
        currentToDoItem = currentToDoItem.copy(title = title)
    }

    fun updatePriority(importance: Importance) {
        currentToDoItem = currentToDoItem.copy(importance = importance)
    }

    fun updateDeadline(deadline: Date?) {
        currentToDoItem = currentToDoItem.copy(deadline = deadline)
    }
}
