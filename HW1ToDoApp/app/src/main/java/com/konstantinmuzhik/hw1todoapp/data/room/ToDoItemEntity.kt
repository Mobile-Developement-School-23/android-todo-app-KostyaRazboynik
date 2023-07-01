package com.konstantinmuzhik.hw1todoapp.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import java.util.Date

@Entity(tableName = "todo_items")
data class ToDoItemEntity(
    @PrimaryKey @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "description") var title: String,
    @ColumnInfo(name = "importance") var priority: Priority,
    @ColumnInfo(name = "deadline") var deadline: Long?,
    @ColumnInfo(name = "done") var done: Boolean,
    @ColumnInfo(name = "createdAt") val createdAt: Long,
    @ColumnInfo(name = "changedAt") var changedAt: Long?
) {
    fun toToDoItem(): ToDoItem =
        ToDoItem(
            id,
            title,
            priority,
            deadline?.let { Date(it) },
            done,
            Date(createdAt),
            changedAt?.let { Date(it) }
        )
    companion object {
        fun fromToDoItem(toDoItem: ToDoItem): ToDoItemEntity =
            ToDoItemEntity(
                id = toDoItem.id,
                title = toDoItem.title,
                priority = toDoItem.priority,
                deadline = toDoItem.deadline?.time,
                done = toDoItem.done,
                createdAt = toDoItem.creationDate.time,
                changedAt = toDoItem.changeDate?.time
            )
    }
}