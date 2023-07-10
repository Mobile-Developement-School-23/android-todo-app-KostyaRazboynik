package com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * ToDoItemRequest
 *
 * @author Konstantin Kovalev
 *
 */
data class ToDoItemRequest(
    @SerializedName("element") val element: ToDoItemNetworkEntity
)