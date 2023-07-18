package com.kostyarazboynik.todoapp.data.repository

import android.util.Log
import com.kostyarazboynik.todoapp.data.datasource.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.data.datasource.remote.ToDoItemApi
import com.kostyarazboynik.todoapp.data.datasource.remote.dto.ToDoItemListRequest
import com.kostyarazboynik.todoapp.data.datasource.remote.dto.ToDoItemNetworkEntity
import com.kostyarazboynik.todoapp.data.datasource.remote.dto.ToDoItemRequest
import com.kostyarazboynik.todoapp.data.models.ToDoItem
import com.kostyarazboynik.todoapp.domain.models.DataState
import com.kostyarazboynik.todoapp.utils.Mappers.modelToNetworkEntity
import com.kostyarazboynik.todoapp.utils.Mappers.networkEntityToModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
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
                Log.d("current token", "!${sharedPreferences.getFullCurrentToken()}!")
                Log.d("30", "getMergedToDoItems start" + currentList.toString())

                val networkListResponse = service.getList(token = sharedPreferences.getFullCurrentToken())

                if (networkListResponse.isSuccessful) {
                    Log.d("35", "successful response start" + networkListResponse.toString())
                    val body = networkListResponse.body()
                    if (body != null) {

                        val revision = body.revision

                        val networkList = body.list.map { it.networkEntityToModel() }
                        Log.d("42", "networkList collected" + networkList.toString())

                        val mergedMap = HashMap<String, ToDoItem>()

                        for (item in currentList) {
                            mergedMap[item.id] = item
                        }
                        for (item in networkList) {
                            if (mergedMap.containsKey(item.id)) {
                                val item1 = mergedMap[item.id]
                                if (item.changedAt!! > item1!!.changedAt) {
                                    mergedMap[item.id] = item
                                } else {
                                    mergedMap[item.id] = item1
                                }
                            } else if (revision != sharedPreferences.getRevisionId()) {
                                mergedMap[item.id] = item
                            }
                        }
                        Log.d("61", "merged map created" + mergedMap.toString())

                        sharedPreferences.putRevisionId(revision)


                        val mergedList = mergedMap.values.toList().map {
                            it.modelToNetworkEntity(deviseId = sharedPreferences.getDeviceId())
                        }
                        Log.d("69", "merged list collected")
                        Log.d("70", mergedList.toString())
                        emitAll(updateRemoteToDoItems(mergedList))
                        Log.d("48", "emitted")
                    }
                } else {
                    networkListResponse.errorBody()?.close()
                }

                Log.d("52", "createMergeList end")
            } catch (exception: SocketTimeoutException) {
                Log.d("get merged to do item e", exception.message.toString())
                emit(DataState.Exception(exception))
            } catch (exception: UnknownHostException) {
                Log.d("get merged to do item e", exception.message.toString())

                emit(DataState.Exception(exception))
            } catch (exception: HttpException) {
                Log.d("get merged to do item e", exception.message.toString())

                emit(DataState.Exception(exception))
            } catch (exception : java.lang.Exception) {
                Log.d("get merged to do item e", exception.message.toString())

                emit(DataState.Exception(exception))
            }
        }

    private suspend fun updateRemoteToDoItems(mergedList: List<ToDoItemNetworkEntity>): Flow<DataState<List<ToDoItem>>> =
        flow {
            try {
                val response = service.updateList(
                    lastKnownRevision = sharedPreferences.getRevisionId(),
                    token = sharedPreferences.getFullCurrentToken(),
                    body = ToDoItemListRequest(mergedList)
                )

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        sharedPreferences.putRevisionId(responseBody.revision)
                        emit(DataState.Result(responseBody.list.map { it.networkEntityToModel() }))
                    }
                }
            } catch (exception: SocketTimeoutException) {
                Log.d("updateRemoteToDoItems", exception.message.toString())
                emit(DataState.Exception(exception))
            } catch (exception: UnknownHostException) {
                Log.d("updateRemoteToDoItems", exception.message.toString())

                emit(DataState.Exception(exception))
            } catch (exception: HttpException) {
                Log.d("updateRemoteToDoItems", exception.message.toString())

                emit(DataState.Exception(exception))
            } catch (exception : java.lang.Exception) {
                Log.d("updateRemoteToDoItems", exception.message.toString())

                emit(DataState.Exception(exception))
            }
        }


    suspend fun updateRemoteToDoItem(toDoItem: ToDoItem) {
        try {
            val response = service.updateToDoItem(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getFullCurrentToken(),
                itemId = toDoItem.id,
                body = ToDoItemRequest(
                    toDoItem.modelToNetworkEntity(sharedPreferences.getDeviceId())
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
        } catch (exception: SocketTimeoutException) {
            Log.d("TAG", exception.toString())
        } catch (exception: UnknownHostException){
            Log.d("TAG", exception.toString())
        } catch (exception: HttpException){
            Log.d("TAG", exception.toString())
        } catch (exception : java.lang.Exception) {
            Log.d("TAG", exception.toString())
        }
    }

    suspend fun deleteRemoteToDoItem(id: String) {
        try {
            val response = service.deleteToDoItem(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getFullCurrentToken(),
                itemId = id
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                }
            } else {
                response.errorBody()?.close()
            }
        } catch (exception: SocketTimeoutException) {
            Log.d("TAG", exception.toString())
        } catch (exception: UnknownHostException){
            Log.d("TAG", exception.toString())
        } catch (exception: HttpException){
            Log.d("TAG", exception.toString())
        } catch (exception : java.lang.Exception) {
            Log.d("TAG", exception.toString())
        }
    }


    suspend fun createRemoteToDoItem(newToDoItem: ToDoItem) {
        try {
            val response = service.addToDoItem(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getFullCurrentToken(),
                newItem = ToDoItemRequest(
                    newToDoItem.modelToNetworkEntity(sharedPreferences.getDeviceId())
                )
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                    Log.d("188", "put revision")

                }
                Log.d("191", "саксес")
            } else Log.d("192", "не саксес")
        } catch (exception: SocketTimeoutException) {
            Log.d("194", exception.toString())
        } catch (exception: UnknownHostException){
            Log.d("196", exception.toString())
        } catch (exception: HttpException){
            Log.d("198", exception.toString())
        } catch (exception : Exception) {
            Log.d("200", exception.toString())
        }
    }
}
