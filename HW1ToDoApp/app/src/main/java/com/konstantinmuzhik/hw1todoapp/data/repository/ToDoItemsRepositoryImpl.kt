package com.konstantinmuzhik.hw1todoapp.data.repository

import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.dao.ToDoItemDao
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.data_mapper.ToDoMapper
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.ToDoItemApi
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.data_mapper.ToDoNetworkMapper
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemListRequest
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemListResponse
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemNetworkEntity
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemRequest
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemResponse
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.domain.models.LoadingState
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHARED_PREFERENCES_NO_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject


/**
 * Repository for ToDoItems
 *
 * @author Konstantin Kovalev
 *
 */
class ToDoItemsRepositoryImpl @Inject constructor(
    private val toDoItemDao: ToDoItemDao,
    private val remoteDataSource: ToDoItemApi,
    private val sharedPreferences: SharedPreferencesAppSettings,
) {
    fun getAllToDoItems(): Flow<List<ToDoItem>> =
        toDoItemDao.getToDoItemsFlow().map { tasks -> tasks.map { ToDoMapper.entityToModel(it) } }

    fun getToDoItemById(id: String): ToDoItem =
        ToDoMapper.entityToModel(toDoItemDao.getToDoItemById(id = id))

    suspend fun updateStatusToDoItem(id: String, done: Boolean) =
        toDoItemDao.updateDone(id, done, System.currentTimeMillis())

    suspend fun updateToDoItem(toDoItem: ToDoItem) =
        toDoItemDao.updateToDoItem(ToDoMapper.modelToEntity(toDoItem))

    suspend fun createItem(toDoItem: ToDoItem) =
        toDoItemDao.createItem(ToDoMapper.modelToEntity(toDoItem))

    suspend fun deleteToDoItem(toDoItem: ToDoItem) =
        toDoItemDao.deleteToDoItem(ToDoMapper.modelToEntity(toDoItem))

    suspend fun deleteAll() = toDoItemDao.deleteAllToDoItems()

    fun deleteToken() = sharedPreferences.setCurrentToken(SHARED_PREFERENCES_NO_TOKEN)

    suspend fun updateRemoteTask(toDoItem: ToDoItem) {
        try {
            val response = createResponse(toDoItem)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) sharedPreferences.putRevisionId(responseBody.revision)
            } else response.errorBody()?.close()
        } catch (_: Exception) {
        }
    }

    suspend fun deleteRemoteTask(taskId: String) {
        try {
            val response = remoteDataSource.deleteToDoItem(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getCurrentToken(),
                itemId = taskId
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) sharedPreferences.putRevisionId(responseBody.revision)
            } else response.errorBody()?.close()
        } catch (_: Exception) {
        }
    }

    suspend fun createRemoteTask(toDoItem: ToDoItem) {
        try {
            val response = createResponse(toDoItem)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) sharedPreferences.putRevisionId(responseBody.revision)
            } else response.errorBody()?.close()
        } catch (_: Exception) {
        }
    }

    private suspend fun updateRemoteTasks(mergedList: List<ToDoItemNetworkEntity>): LoadingState<Any> {
        try {
            val response = createListResponse(mergedList)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                    toDoItemDao.mergeToDoItems(responseBody.list.map {
                        ToDoMapper.modelToEntity(ToDoNetworkMapper.networkEntityToModel(it))
                    })
                    return LoadingState.Success(responseBody.list)
                }
            } else response.errorBody()?.close()
        } catch (_: Exception) {
            return LoadingState.Error("Merge failed, continue offline.")
        }
        return LoadingState.Error("Merge failed, continue offline.")
    }

    suspend fun getRemoteTasks(): LoadingState<Any> {
        try {
            val networkListResponse =
                remoteDataSource.getList(token = sharedPreferences.getCurrentToken())

            if (networkListResponse.isSuccessful) {
                val body = networkListResponse.body()
                if (body != null)
                    return updateRemoteTasks(createToDoItemMergedList(body).values.toList())
            } else networkListResponse.errorBody()?.close()

        } catch (_: Exception) {
            return LoadingState.Error("Merge failed, continue offline.")
        }
        return LoadingState.Error("Merge failed, continue offline.")
    }

    private suspend fun createResponse(toDoItem: ToDoItem): Response<ToDoItemResponse> =
        remoteDataSource.updateToDoItem(
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

    private suspend fun createListResponse(mergedList: List<ToDoItemNetworkEntity>): Response<ToDoItemListResponse> =
        remoteDataSource.updateList(
            lastKnownRevision = sharedPreferences.getRevisionId(),
            token = sharedPreferences.getCurrentToken(),
            body = ToDoItemListRequest(status = "ok", mergedList)
        )

    private fun createToDoItemMergedList(body: ToDoItemListResponse):  HashMap<String, ToDoItemNetworkEntity> {
        val revision = body.revision
        val networkList = body.list
        val currentList = createToDoItemNetworkEntityList()
        val mergedList = HashMap<String, ToDoItemNetworkEntity>()

        for (item in currentList) mergedList[item.id] = item
        for (item in networkList) {
            if (mergedList.containsKey(item.id)) {
                val item1 = mergedList[item.id]
                if (item.changedAt > item1!!.changedAt) mergedList[item.id] = item
                else mergedList[item.id] = item1
            } else if (revision != sharedPreferences.getRevisionId())
                mergedList[item.id] = item
        }

        return mergedList
    }

    private fun createToDoItemNetworkEntityList(): List<ToDoItemNetworkEntity> =
        toDoItemDao.getToDoItems().map {
            ToDoNetworkMapper.modelToNetworkEntity(
                ToDoMapper.entityToModel(it),
                sharedPreferences.getDeviceId()
            )
        }
}