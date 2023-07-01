package com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.request

import com.google.gson.annotations.SerializedName
import com.konstantinmuzhik.hw1todoapp.data.retrofit.dto.ToDoItemResponseRequest

data class ToDoItemApiRequestElement(
    @SerializedName("element")
    val element: ToDoItemResponseRequest
)