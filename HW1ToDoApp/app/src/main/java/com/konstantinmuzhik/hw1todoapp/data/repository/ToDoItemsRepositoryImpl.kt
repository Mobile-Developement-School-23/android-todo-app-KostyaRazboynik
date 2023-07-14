package com.konstantinmuzhik.hw1todoapp.data.repository

import com.konstantinmuzhik.hw1todoapp.data.datasource.local.dao.ToDoItemDao
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.data_mapper.ToDoMapper
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.domain.models.DataState
import com.konstantinmuzhik.hw1todoapp.domain.models.UiState
import com.konstantinmuzhik.hw1todoapp.domain.repository.Repository
import com.konstantinmuzhik.hw1todoapp.ui.notifications.NotificationsSchedulerImpl
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
    private val notificationsScheduler: NotificationsSchedulerImpl
): Repository {

    override fun getToDoItemsFlow(): Flow<UiState<List<ToDoItem>>> = flow {
        emit(UiState.Start)
        toDoItemDao.getToDoItemsFlow().collect { list ->
            emit(UiState.Success(list.map {ToDoMapper.entityToModel(it)}))
        }
    }

    override suspend fun createToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.createToDoItem(ToDoMapper.modelToEntity(toDoItem))
        networkSource.createRemoteToDoItem(toDoItem)
        notificationsScheduler.schedule(toDoItem)
    }

    override suspend fun deleteToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.deleteToDoItem(ToDoMapper.modelToEntity(toDoItem))
        networkSource.deleteRemoteToDoItem(toDoItem.id)
        notificationsScheduler.cancel(toDoItem.id)
    }

    override suspend fun updateToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.updateToDoItem(ToDoMapper.modelToEntity(toDoItem))
        networkSource.updateRemoteToDoItem(toDoItem)
        notificationsScheduler.schedule(toDoItem)
    }

    override fun getRemoteToDoItemsFlow(): Flow<UiState<List<ToDoItem>>> = flow {
        emit(UiState.Start)
        networkSource.getMergedToDoItems(
            toDoItemDao.getToDoItems().map { ToDoMapper.entityToModel(it) })
            .collect { state ->
                when (state) {
                    DataState.Initial -> emit(UiState.Start)
                    is DataState.Exception -> emit(UiState.Error(state.cause.message.toString()))
                    is DataState.Result -> {
                        toDoItemDao.mergeToDoItems(state.data.map { ToDoMapper.modelToEntity(it) })
                        emit(UiState.Success(state.data))
                        updateNotifications(state.data)
                    }
                }
            }
    }

    override fun getToDoItemById(id: String): ToDoItem =
        ToDoMapper.entityToModel(toDoItemDao.getToDoItemById(id))

    override suspend fun deleteCurrentToDoItems() = toDoItemDao.deleteAllToDoItems()

    private fun updateNotifications(items: List<ToDoItem>) {
        notificationsScheduler.cancelAll()
        for (item in items){
            notificationsScheduler.schedule(item)
        }
    }
}