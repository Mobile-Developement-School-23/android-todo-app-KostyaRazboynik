package com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * ToDoItemListResponse
 *
 * @author Konstantin Kovalev
 *
 */
data class ToDoItemListResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("revision")
    val revision: Int,
    @SerializedName("list")
    val list: List<ToDoItemNetworkEntity>
)
