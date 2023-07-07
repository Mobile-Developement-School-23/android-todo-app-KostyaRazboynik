package com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.PriorityAdapter
import com.konstantinmuzhik.hw1todoapp.data.models.Priority

data class ToDoItemNetworkEntity(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("priority")
    @JsonAdapter(PriorityAdapter::class) val priority: Priority,
    @SerializedName("deadline") val deadline: Long?,
    @SerializedName("done") val done: Boolean,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("changedAt") val changedAt: Long,
    @SerializedName("color") val color: String?,
    @SerializedName("last_updated_by") val last_updated_by: String = "this",
)