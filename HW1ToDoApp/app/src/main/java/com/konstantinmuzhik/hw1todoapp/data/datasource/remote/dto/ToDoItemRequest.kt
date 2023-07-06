package com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class ToDoItemRequest(
    @SerializedName("element") val element: ToDoItemNetworkEntity
)