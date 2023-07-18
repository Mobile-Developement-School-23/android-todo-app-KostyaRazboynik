package com.kostyarazboynik.todoapp.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * ToDoItemListRequest
 *
 * @author Konstantin Kovalev
 *
 */
data class ToDoItemListRequest(
    @SerializedName("list")
    val list: List<ToDoItemNetworkEntity>
)