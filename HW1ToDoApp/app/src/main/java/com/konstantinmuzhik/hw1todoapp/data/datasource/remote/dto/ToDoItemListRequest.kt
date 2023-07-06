package com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class ToDoItemListRequest(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<ToDoItemNetworkEntity>
)