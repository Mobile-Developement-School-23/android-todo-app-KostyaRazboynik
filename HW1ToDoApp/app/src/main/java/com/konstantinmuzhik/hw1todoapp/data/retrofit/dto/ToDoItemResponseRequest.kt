package com.konstantinmuzhik.hw1todoapp.data.retrofit.dto

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.konstantinmuzhik.hw1todoapp.data.retrofit.PriorityAdapter
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import java.util.Date

data class ToDoItemResponseRequest(
    @SerializedName("id")
    val id: String,

    @SerializedName("deadline")
    val deadline: Long?,

    @SerializedName("done")
    val done: Boolean,

    @SerializedName("color")
    val color: String?,

    @SerializedName("priority")
    @JsonAdapter(PriorityAdapter::class)
    val priority: Priority,

    @SerializedName("created_at")
    val created_at: Long,

    @SerializedName("changed_at")
    val changed_at: Long,

    @SerializedName("last_updated_by")
    val last_updated_by: String,

    @SerializedName("title")
    val title: String
) {
    fun toToDoItem(): ToDoItem = ToDoItem(
        id,
        title,
        priority,
        deadline?.let { Date(it) },
        done,
        Date(created_at),
        Date(changed_at)
    )
    companion object {
        fun fromToDoTask(toDoItem: ToDoItem, deviseId: String): ToDoItemResponseRequest {
            return ToDoItemResponseRequest(
                id = toDoItem.id,
                title = toDoItem.title,
                priority = toDoItem.priority,
                deadline = toDoItem.deadline?.time,
                done = toDoItem.done,
                created_at = toDoItem.creationDate.time,
                changed_at = toDoItem.changeDate?.time ?: 0,
                last_updated_by = deviseId,
                color = null
            )
        }
    }
}