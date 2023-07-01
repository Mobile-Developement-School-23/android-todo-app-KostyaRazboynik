package com.konstantinmuzhik.hw1todoapp.data.repository

import android.util.Log
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.models.LoadingState
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.retrofit.ToDoItemApi
import com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.ToDoItemResponseRequest
import com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.request.ToDoItemApiRequestElement
import com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.request.ToDoItemApiRequestList
import com.konstantinmuzhik.hw1todoapp.data.room.ToDoItemDatabase
import com.konstantinmuzhik.hw1todoapp.data.room.ToDoItemEntity
import com.konstantinmuzhik.hw1todoapp.utils.Constants.NO_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ToDoItemsRepository(
    localDataSource: ToDoItemDatabase,
    private val remoteDataSource: ToDoItemApi,
    private val sharedPreferences: SharedPreferencesAppSettings
) {

    private val toDoItemDAO = localDataSource.toDoItemDao()
    private val LOG_TAG = ToDoItemsRepository::class.simpleName.toString()

    fun getAllToDoItems(): Flow<List<ToDoItem>> =
        toDoItemDAO.getToDoItems().map { it -> it.map { it.toToDoItem() } }

    fun getToDoItemById(id: String): ToDoItem =
        toDoItemDAO.getToDoItemById(id = id).toToDoItem()

    suspend fun updateToDoItemDone(id: String, done: Boolean) =
        toDoItemDAO.updateDone(id, done, System.currentTimeMillis())

    suspend fun updateToDoItem(toDoItem: ToDoItem) =
        toDoItemDAO.updateToDoItem(ToDoItemEntity.fromToDoItem(toDoItem))

    suspend fun createToDoItem(toDoItem: ToDoItem) =
        toDoItemDAO.createItem(ToDoItemEntity.fromToDoItem(toDoItem))

    suspend fun deleteToDoItem(toDoItem: ToDoItem) =
        toDoItemDAO.deleteToDoItem(ToDoItemEntity.fromToDoItem(toDoItem))

    suspend fun deleteAllToDoItems() = toDoItemDAO.deleteAllToDoItems()

    fun deleteToken() = sharedPreferences.setCurrentToken(NO_TOKEN)

    suspend fun updateRemoteToDoItem(toDoItem: ToDoItem) {
        try {
            val response = remoteDataSource.updateToDoItem(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getCurrentToken(),
                itemId = toDoItem.id,
                body = ToDoItemApiRequestElement(
                    ToDoItemResponseRequest.fromToDoTask(
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
                newItem = ToDoItemApiRequestElement(
                    ToDoItemResponseRequest.fromToDoTask(
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

    private suspend fun updateRemoteToDoItem(mergedList: List<ToDoItemResponseRequest>): LoadingState<Any> {
        try {
            val response = remoteDataSource.updateList(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getCurrentToken(),
                body = ToDoItemApiRequestList(status = "ok", mergedList)
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                    toDoItemDAO.mergeToDoItems(responseBody.list.map {
                        ToDoItemEntity.fromToDoItem(it.toToDoItem())
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
            val networkListResponse = remoteDataSource.getList(token = sharedPreferences.getCurrentToken())

            if (networkListResponse.isSuccessful) {
                val body = networkListResponse.body()
                if (body != null) {
                    val revision = body.revision
                    val networkList = body.list
                    val currentList = toDoItemDAO.getToDoItemsNoFlow().map {
                        ToDoItemResponseRequest.fromToDoTask(
                            it.toToDoItem(),
                            sharedPreferences.getDeviceId()
                        )
                    }
                    val mergedList = HashMap<String, ToDoItemResponseRequest>()

                    for (item in currentList) {
                        mergedList[item.id] = item
                    }
                    for (item in networkList) {
                        if (mergedList.containsKey(item.id)) {
                            val item1 = mergedList[item.id]
                            if (item.changed_at > item1!!.changed_at) mergedList[item.id] = item
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