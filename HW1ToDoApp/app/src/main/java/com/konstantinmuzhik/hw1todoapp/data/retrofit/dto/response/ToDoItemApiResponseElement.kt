package com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.response

import com.google.gson.annotations.SerializedName
import com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.ToDoItemResponseRequest

data class ToDoItemApiResponseElement(
    @SerializedName("revision")
    val revision: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("element")
    val element: ToDoItemResponseRequest
)