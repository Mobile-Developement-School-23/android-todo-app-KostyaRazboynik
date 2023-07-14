package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepositoryImpl
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

    fun updatePriority(priority: Priority) {
        currentToDoItem = currentToDoItem.copy(priority = priority)
    }

    fun updateDeadline(deadline: Date?) {
        currentToDoItem = currentToDoItem.copy(deadline = deadline)
    }
}
