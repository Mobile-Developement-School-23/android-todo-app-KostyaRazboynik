package com.kostyarazboynik.todoapp.domain.repository

import com.kostyarazboynik.todoapp.data.models.ToDoItem
import com.kostyarazboynik.todoapp.domain.models.UiState
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getToDoItemsFlow(): Flow<UiState<List<ToDoItem>>>

    fun getToDoItemById(id: String): ToDoItem

    suspend fun createToDoItem(toDoItem: ToDoItem)

    suspend fun deleteToDoItem(toDoItem: ToDoItem)

    suspend fun updateToDoItem(toDoItem: ToDoItem)

    fun getRemoteToDoItemsFlow(): Flow<UiState<List<ToDoItem>>>

    suspend fun deleteCurrentToDoItems()
}