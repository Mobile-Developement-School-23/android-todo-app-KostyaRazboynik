package com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class ToDoItemResponse(
    @SerializedName("revision") val revision: Int,
    @SerializedName("status") val status: String,
    @SerializedName("element") val element: ToDoItemNetworkEntity
)