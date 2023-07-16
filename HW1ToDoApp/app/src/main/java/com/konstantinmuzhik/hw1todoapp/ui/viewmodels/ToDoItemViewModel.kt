package com.konstantinmuzhik.hw1todoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepositoryImpl
import com.konstantinmuzhik.hw1todoapp.domain.models.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ToDoItemViewModel @Inject constructor(
    private val repository: ToDoItemsRepositoryImpl,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _todoItem:MutableStateFlow<UiState<ToDoItem>> = MutableStateFlow(UiState.Start)

    val todoItem = _todoItem.asStateFlow()

    fun getToDoItem(id:String) {
        if (todoItem.value == UiState.Start)
            coroutineScope.launch(Dispatchers.IO) {
                _todoItem.emit(UiState.Success(repository.getToDoItemById(id)))
            }
    }

    fun setToDoItem() {
        if (todoItem.value == UiState.Start)
            _todoItem.value = UiState.Success(ToDoItem())
    }

    fun addToDoItem(todoItem: ToDoItem) =
        coroutineScope.launch(Dispatchers.IO) {
            repository.createToDoItem(todoItem.copy())
        }

    fun deleteToDoItem(todoItem: ToDoItem) =
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteToDoItem(todoItem)
        }

    fun updateToDoItem(task: ToDoItem) {
        task.changedAt?.time = System.currentTimeMillis()
        coroutineScope.launch(Dispatchers.IO) {
            repository.updateToDoItem(task)
        }
    }
}
