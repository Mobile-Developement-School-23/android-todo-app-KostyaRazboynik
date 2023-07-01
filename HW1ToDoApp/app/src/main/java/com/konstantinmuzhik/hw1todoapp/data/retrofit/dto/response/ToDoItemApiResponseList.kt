package com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.response

import com.google.gson.annotations.SerializedName
import com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.ToDoItemResponseRequest

data class ToDoItemApiResponseList(
    @SerializedName("status")
    val status: String,
    @SerializedName("revision")
    val revision: Int,
    @SerializedName("list")
    val list: List<ToDoItemResponseRequest>
)
