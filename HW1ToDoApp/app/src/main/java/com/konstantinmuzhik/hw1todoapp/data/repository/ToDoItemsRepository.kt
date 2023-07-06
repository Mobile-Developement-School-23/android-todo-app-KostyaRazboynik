package com.konstantinmuzhik.hw1todoapp.data.repository

import android.util.Log
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.ToDoItemDatabase
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.data_mapper.ToDoMapper
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.ToDoItemApi
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.data_mapper.ToDoNetworkMapper
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemListRequest
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemNetworkEntity
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemRequest
import com.konstantinmuzhik.hw1todoapp.data.models.LoadingState
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.di.scope.AppScope
import com.konstantinmuzhik.hw1todoapp.utils.Constants.NO_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AppScope
class ToDoItemsRepository @Inject constructor(
    localDataSource: ToDoItemDatabase,
    private val remoteDataSource: ToDoItemApi,
    private val sharedPreferences: SharedPreferencesAppSettings,
) {

    private val toDoItemDAO = localDataSource.toDoItemDao()
    private val LOG_TAG = ToDoItemsRepository::class.simpleName.toString()

    fun getAllToDoItems(): Flow<List<ToDoItem>> =
        toDoItemDAO.getToDoItemsFlow().map { it -> it.map { ToDoMapper.entityToModel(it) } }

    fun getToDoItemById(id: String): ToDoItem =
        ToDoMapper.entityToModel(toDoItemDAO.getToDoItemById(id = id))

    suspend fun updateToDoItemDone(id: String, done: Boolean) =
        toDoItemDAO.updateDone(id, done, System.currentTimeMillis())

    suspend fun updateToDoItem(toDoItem: ToDoItem) =
        toDoItemDAO.updateToDoItem(ToDoMapper.modelToEntity(toDoItem))

    suspend fun createToDoItem(toDoItem: ToDoItem) =
        toDoItemDAO.createItem(ToDoMapper.modelToEntity(toDoItem))

    suspend fun deleteToDoItem(toDoItem: ToDoItem) =
        toDoItemDAO.deleteToDoItem(ToDoMapper.modelToEntity(toDoItem))

    suspend fun deleteAllToDoItems() = toDoItemDAO.deleteAllToDoItems()

    fun deleteToken() = sharedPreferences.setCurrentToken(NO_TOKEN)

    suspend fun updateRemoteToDoItem(toDoItem: ToDoItem) {
        try {
            val response = remoteDataSource.updateToDoItem(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getCurrentToken(),
                itemId = toDoItem.id,
                body = ToDoItemRequest(
                    ToDoNetworkMapper.modelToNetworkEntity(
                        toDoItem,
                        sharedPreferences.getDeviceId()
                    )
                )
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) sharedPreferences.putRevisionId(responseBody.revision)
            } else response.errorBody()?.close()
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.message.toString())
        }
    }

    suspend fun deleteRemoteToDoItem(taskId: String) {
        try {
            val response = remoteDataSource.deleteToDoItem(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getCurrentToken(),
                itemId = taskId
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null)
                    sharedPreferences.putRevisionId(responseBody.revision)
            } else response.errorBody()?.close()
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.toString())
        }
    }

    suspend fun createRemoteToDoItem(newTask: ToDoItem) {
        try {
            val response = remoteDataSource.addToDoItem(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getCurrentToken(),
                newItem = ToDoItemRequest(
                    ToDoNetworkMapper.modelToNetworkEntity(
                        newTask,
                        sharedPreferences.getDeviceId()
                    )
                )
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) sharedPreferences.putRevisionId(responseBody.revision)
            } else response.errorBody()?.close()
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.toString())
        }
    }

    private suspend fun updateRemoteToDoItem(mergedList: List<ToDoItemNetworkEntity>): LoadingState<Any> {
        try {
            val response = remoteDataSource.updateList(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getCurrentToken(),
                body = ToDoItemListRequest(status = "ok", mergedList)
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                    toDoItemDAO.mergeToDoItems(responseBody.list.map {
                        ToDoMapper.modelToEntity(ToDoNetworkMapper.networkEntityToModel(it))
                    })
                    return LoadingState.Success(responseBody.list)
                }
            } else response.errorBody()?.close()
        } catch (e: Exception) {
            return LoadingState.Error("Merge failed, continue offline.")
        }

        return LoadingState.Error("Merge failed, continue offline.")
    }

    suspend fun getRemoteToDoItems(): LoadingState<Any> {
        try {
            val networkListResponse =
                remoteDataSource.getList(token = sharedPreferences.getCurrentToken())

            if (networkListResponse.isSuccessful) {
                val body = networkListResponse.body()
                if (body != null) {
                    val revision = body.revision
                    val networkList = body.list
                    val currentList = toDoItemDAO.getToDoItems().map {
                        ToDoNetworkMapper.modelToNetworkEntity(
                            ToDoMapper.entityToModel(it),
                            sharedPreferences.getDeviceId()
                        )
                    }
                    val mergedList = HashMap<String, ToDoItemNetworkEntity>()

                    for (item in currentList) {
                        mergedList[item.id] = item
                    }
                    for (item in networkList) {
                        if (mergedList.containsKey(item.id)) {
                            val item1 = mergedList[item.id]
                            if (item.changedAt > item1!!.changedAt) mergedList[item.id] = item
                            else mergedList[item.id] = item1
                        } else if (revision != sharedPreferences.getRevisionId())
                            mergedList[item.id] = item
                    }

                    return updateRemoteToDoItem(mergedList.values.toList())
                }
            } else networkListResponse.errorBody()?.close()

        } catch (e: Exception) {
            return LoadingState.Error("Merge failed, continue offline.")
        }
        return LoadingState.Error("Merge failed, continue offline.")
    }
}