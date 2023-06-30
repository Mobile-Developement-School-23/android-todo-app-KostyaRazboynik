package com.konstantinmuzhik.hw1todoapp.data.repository

import android.util.Log
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.local.dao.database.ToDoItemDatabase
import com.konstantinmuzhik.hw1todoapp.data.local.dao.entity.ToDoItemEntity
import com.konstantinmuzhik.hw1todoapp.data.models.LoadingState
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.retrofit.ToDoApiRequestElement
import com.konstantinmuzhik.hw1todoapp.data.retrofit.ToDoApiRequestList
import com.konstantinmuzhik.hw1todoapp.data.retrofit.ToDoItemApi
import com.konstantinmuzhik.hw1todoapp.data.retrofit.ToDoItemResponseRequest
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

    fun getNotCompletedToDoItems(): Flow<List<ToDoItem>> =
        toDoItemDAO.getNotCompletedToDoItems().map { it -> it.map { it.toToDoItem() } }

    fun getToDoItemById(id: String): ToDoItem =
        toDoItemDAO.getToDoItemById(id = id).toToDoItem()

    suspend fun updateStatusToDoItem(id: String, done: Boolean) =
        toDoItemDAO.updateDone(id, done, System.currentTimeMillis())

    suspend fun updateToDoItem(toDoItem: ToDoItem) =
        toDoItemDAO.updateToDoItem(ToDoItemEntity.fromToDoItem(toDoItem))

    suspend fun createItem(toDoItem: ToDoItem) =
        toDoItemDAO.createItem(ToDoItemEntity.fromToDoItem(toDoItem))

    suspend fun deleteToDoItem(toDoItem: ToDoItem) =
        toDoItemDAO.deleteToDoItem(ToDoItemEntity.fromToDoItem(toDoItem))

    suspend fun updateRemoteTask(toDoTask: ToDoItem) {
        try {
            val response = remoteDataSource.updateTask(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                itemId = toDoTask.id,
                ToDoApiRequestElement(
                    ToDoItemResponseRequest.fromToDoTask(
                        toDoTask,
                        sharedPreferences.getDeviceId()
                    )
                )
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                }
            } else {
                response.errorBody()?.close()
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.message.toString())
        }


    }

    suspend fun deleteRemoteTask(taskId: String) {
        try {
            val response = remoteDataSource.deleteTask(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                itemId = taskId
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                }
            } else {
                response.errorBody()?.close()
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.toString())
        }

    }

    suspend fun createRemoteTask(newTask: ToDoItem) {
        try {
            val response = remoteDataSource.addTask(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                ToDoApiRequestElement(
                    ToDoItemResponseRequest.fromToDoTask(
                        newTask,
                        sharedPreferences.getDeviceId()
                    )
                )
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                }
            } else {
                response.errorBody()?.close()
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.toString())
        }
    }

    private suspend fun updateRemoteTasks(mergedList: List<ToDoItemResponseRequest>): LoadingState<Any> {
        try {
            val response = remoteDataSource.updateList(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                ToDoApiRequestList(status = "ok", mergedList)
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
            } else {
                response.errorBody()?.close()
            }
        } catch (e: Exception) {
            return LoadingState.Error("Merge failed, continue offline.")
        }

        return LoadingState.Error("Merge failed, continue offline.")
    }

    suspend fun getRemoteTasks(): LoadingState<Any> {
        try {
            val networkListResponse = remoteDataSource.getList()

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
                            if (item.changed_at > item1!!.changed_at) {
                                mergedList[item.id] = item
                            } else {
                                mergedList[item.id] = item1
                            }
                        } else if (revision != sharedPreferences.getRevisionId()) {
                            mergedList[item.id] = item
                        }
                    }

                    return updateRemoteTasks(mergedList.values.toList())
                }
            }

        } catch (e: Exception) {
            return LoadingState.Error("Merge failed, continue offline.")
        }
        return LoadingState.Error("Merge failed, continue offline.")
    }

}