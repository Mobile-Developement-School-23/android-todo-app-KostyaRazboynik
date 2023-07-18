package com.kostyarazboynik.todoapp.data.datasource.remote.dto

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.kostyarazboynik.todoapp.data.datasource.remote.utils.PriorityAdapter
import com.kostyarazboynik.todoapp.data.models.Importance
import com.kostyarazboynik.todoapp.data.models.ToDoItem
import java.util.Date


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

    @SerializedName("color")
    val color: String?,

    @SerializedName("importance")
    val importance: String,

    @SerializedName("created_at")
    val createdAt: Long,

    @SerializedName("changed_at")
    val changedAt: Long,

    @SerializedName("last_updated_by")
    val lastUpdatedBy: String,

    @SerializedName("text")
    val title: String
)