package com.konstantinmuzhik.hw1todoapp.data.retrofit

import com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.request.ToDoItemApiRequestElement
import com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.request.ToDoItemApiRequestList
import com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.response.ToDoItemApiResponseElement
import com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.response.ToDoItemApiResponseList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ToDoItemApi {
    @GET("list")
    suspend fun getList(@Header("Authorization") token: String): Response<ToDoItemApiResponseList>

    @PATCH("list")
    suspend fun updateList(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body body: ToDoItemApiRequestList
    ): Response<ToDoItemApiResponseList>

    @POST("list")
    suspend fun addToDoItem(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body newItem: ToDoItemApiRequestElement
    ): Response<ToDoItemApiResponseElement>

    @PUT("list/{id}")
    suspend fun updateToDoItem(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Path("id") itemId: String,
        @Body body: ToDoItemApiRequestElement
    ): Response<ToDoItemApiResponseElement>

    @DELETE("list/{id}")
    suspend fun deleteToDoItem(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Path("id") itemId: String,
    ): Response<ToDoItemApiResponseElement>
}