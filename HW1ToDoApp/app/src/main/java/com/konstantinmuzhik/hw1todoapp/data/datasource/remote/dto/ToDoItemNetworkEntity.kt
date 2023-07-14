package com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName
import com.konstantinmuzhik.hw1todoapp.data.models.Priority


/**
 * Network Entity for ToDoItem
 *
 * @author Konstantin Kovalev
 *
 */
data class ToDoItemNetworkEntity(
    @SerializedName("id")
    val id: String,
    @SerializedName("deadline")
    val deadline: Long?,
    @SerializedName("done")
    val done: Boolean,
    @SerializedName("importance")
    val priority: Priority,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("changed_at")
    val changedAt: Long,
    @SerializedName("last_updated_by")
    val lastUpdatedBy: String,
    @SerializedName("text")
    val title: String
)