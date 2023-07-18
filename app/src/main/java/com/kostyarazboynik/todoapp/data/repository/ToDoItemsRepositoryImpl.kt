package com.kostyarazboynik.todoapp.data.repository

import android.util.Log
import com.kostyarazboynik.todoapp.data.datasource.local.dao.ToDoItemDao
import com.kostyarazboynik.todoapp.data.models.ToDoItem
import com.kostyarazboynik.todoapp.domain.models.DataState
import com.kostyarazboynik.todoapp.domain.models.UiState
import com.kostyarazboynik.todoapp.domain.notifications.NotificationsSchedulerImpl
import com.kostyarazboynik.todoapp.domain.repository.Repository
import com.kostyarazboynik.todoapp.utils.Mappers.entityToModel
import com.kostyarazboynik.todoapp.utils.Mappers.modelToEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


/**
 * Repository for ToDoItems
 *
 * @author Konstantin Kovalev
 *
 */
class ToDoItemsRepositoryImpl @Inject constructor(
    private val toDoItemDao: ToDoItemDao,
    private val networkSource: RemoteToDoItemsRepositoryImpl,
    private val notificationsScheduler: NotificationsSchedulerImpl,
) : Repository {

    override fun getToDoItemsFlow(): Flow<UiState<List<ToDoItem>>> = flow {
        emit(UiState.Start)
        toDoItemDao.getToDoItemsFlow().collect { list ->
            emit(UiState.Success(list.map { it.entityToModel() }))
        }
    }

    override suspend fun createToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.createToDoItem(toDoItem.modelToEntity())
        networkSource.createRemoteToDoItem(toDoItem)
        notificationsScheduler.schedule(toDoItem)
    }

    override suspend fun deleteToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.deleteToDoItem(toDoItem.modelToEntity())
        networkSource.deleteRemoteToDoItem(toDoItem.id)
        notificationsScheduler.cancel(toDoItem.id)
    }

    override suspend fun updateToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.updateToDoItem(toDoItem.modelToEntity())
        networkSource.updateRemoteToDoItem(toDoItem)
        notificationsScheduler.schedule(toDoItem)
    }

    override fun getRemoteToDoItemsFlow(): Flow<UiState<List<ToDoItem>>> = flow {
        emit(UiState.Start)
        Log.d("56", "getRemoteToDoItemsFlow start")
        val list = networkSource.getMergedToDoItems(toDoItemDao.getToDoItems().map { it.entityToModel() })
        Log.d("58", list.toString())

        list.collect { state ->
                when (state) {
                    DataState.Initial -> emit(UiState.Start)
                    is DataState.Exception -> emit(UiState.Error(state.cause.message.toString()))
                    is DataState.Result -> {
                        toDoItemDao.mergeToDoItems(state.data.map { it.modelToEntity() })
                        emit(UiState.Success(state.data))
                        updateNotifications(state.data)
                    }
                }
            }
    }

    override fun getToDoItemById(id: String): ToDoItem =
        toDoItemDao.getToDoItemById(id).entityToModel()

    override suspend fun deleteCurrentToDoItems() = toDoItemDao.deleteAllToDoItems()

    private fun updateNotifications(items: List<ToDoItem>) {
        notificationsScheduler.cancelAll()
        for (item in items) notificationsScheduler.schedule(item)
    }
}