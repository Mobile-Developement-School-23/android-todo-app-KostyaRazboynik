package com.konstantinmuzhik.hw1todoapp.data.repository

import android.util.Log
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.ToDoItemApi
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.data_mapper.ToDoNetworkMapper
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemListRequest
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemListResponse
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemNetworkEntity
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemRequest
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemResponse
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.domain.models.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RemoteToDoItemsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferencesAppSettings,
    private val service: ToDoItemApi,
) {

    suspend fun getMergedToDoItems(currentList: List<ToDoItem>): Flow<DataState<List<ToDoItem>>> =
        flow {
            try {
                val token = sharedPreferences.getCurrentToken()
                val networkListResponse = service.getList(token = token)

                Log.d("LIST CALLED", "")

                createMergedList(currentList, networkListResponse)
                Log.d("LIST MERGED FINAL", "")
                Log.d("createMergedList", "created")


            } catch (exception: Exception) {
                Log.d("EXCEPTION1", exception.toString())
                emit(DataState.Exception(exception))
            }
        }

    private suspend fun createMergedList(
        currentList: List<ToDoItem>,
        networkListResponse: Response<ToDoItemListResponse>,
    ): Flow<DataState<List<ToDoItem>>> = flow {
        if (networkListResponse.isSuccessful) {
            Log.d("SUCCES 1", "")
            val body = networkListResponse.body()
            if (body != null) {
                val networkList = body.list.map { ToDoNetworkMapper.networkEntityToModel(it) }

                val mergedList =
                    createMergedMap(currentList, networkList, body.revision).values.toList().map {
                        ToDoNetworkMapper.modelToNetworkEntity(
                            it,
                            deviseId = sharedPreferences.getDeviceId()
                        )
                    }
                Log.d("LIST MERGED PRE FINAL", "")


                emitAll(updateRemoteToDoItems(mergedList))
            }
        } else networkListResponse.errorBody()?.close()
    }

    private fun createMergedMap(
        currentList: List<ToDoItem>,
        networkList: List<ToDoItem>,
        revision: Int,
    ): HashMap<String, ToDoItem> {
        val mergedMap = HashMap<String, ToDoItem>()

        for (item in currentList) mergedMap[item.id] = item
        for (item in networkList) {
            if (mergedMap.containsKey(item.id)) {
                val item1 = mergedMap[item.id]
                if (item.changedAt!! > item1!!.changedAt) mergedMap[item.id] = item
                else mergedMap[item.id] = item1
            } else if (revision != sharedPreferences.getRevisionId())
                mergedMap[item.id] = item
        }
        sharedPreferences.putRevisionId(revision)
        Log.d("REVISION PUT", "")

        return mergedMap
    }

    private suspend fun updateRemoteToDoItems(mergedList: List<ToDoItemNetworkEntity>): Flow<DataState<List<ToDoItem>>> =
        flow {
            try {
                val response = service.updateList(
                    lastKnownRevision = sharedPreferences.getRevisionId(),
                    token = sharedPreferences.getCurrentToken(),
                    body = ToDoItemListRequest(mergedList)
                )

                if (response.isSuccessful) emitRemoteTasks(response)
            } catch (exception: Exception) {
                emit(DataState.Exception(exception))
            }
        }

    private suspend fun emitRemoteTasks(response: Response<ToDoItemListResponse>): Flow<DataState<List<ToDoItem>>> =
        flow {
            val responseBody = response.body()
            if (responseBody != null) {
                sharedPreferences.putRevisionId(responseBody.revision)
                emit(DataState.Result(responseBody.list.map {
                    ToDoNetworkMapper.networkEntityToModel(it)
                }))
            }
        }


    suspend fun updateRemoteToDoItem(toDoItem: ToDoItem) {
        try {
            val response = createUpdateResponse(toDoItem)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) sharedPreferences.putRevisionId(responseBody.revision)
            } else response.errorBody()?.close()
        } catch (_: Exception) {
        }
    }

    private suspend fun createUpdateResponse(toDoItem: ToDoItem): Response<ToDoItemResponse> =
        service.updateToDoItem(
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

    suspend fun deleteRemoteToDoItem(id: String) {
        try {
            val response = createDeleteResponse(id)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) sharedPreferences.putRevisionId(responseBody.revision)
            } else response.errorBody()?.close()
        } catch (_: Exception) {
        }
    }

    private suspend fun createDeleteResponse(id: String): Response<ToDoItemResponse> =
        service.deleteToDoItem(
            lastKnownRevision = sharedPreferences.getRevisionId(),
            token = sharedPreferences.getCurrentToken(),
            itemId = id
        )

    // TODO
    suspend fun createRemoteToDoItem(newToDoItem: ToDoItem) {
        try {
            val response = createAddResponse(newToDoItem)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) sharedPreferences.putRevisionId(responseBody.revision)
                Log.d("created Task", "АХУЙ")
            }
        } catch (_: Exception) {
        }
    }

    private suspend fun createAddResponse(newToDoItem: ToDoItem): Response<ToDoItemResponse> {
        val token = sharedPreferences.getCurrentToken()
        val rev = sharedPreferences.getRevisionId()

        val s = ToDoNetworkMapper.modelToNetworkEntity(
            newToDoItem,
            sharedPreferences.getDeviceId()
        )
        val request = ToDoItemRequest(s)
        val res =  service.addToDoItem(
            lastKnownRevision = rev,
            token = token,
            newItem = request

        )
        Log.d("RESULT", res.isSuccessful.toString())
        return res
    }

}
