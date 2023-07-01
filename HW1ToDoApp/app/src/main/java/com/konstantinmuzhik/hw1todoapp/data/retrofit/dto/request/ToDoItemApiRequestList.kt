package com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.request

import com.google.gson.annotations.SerializedName
import com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.ToDoItemResponseRequest

data class ToDoItemApiRequestList(
    @SerializedName("status")
    val status: String,
    @SerializedName("list")
    val list: List<ToDoItemResponseRequest>
)