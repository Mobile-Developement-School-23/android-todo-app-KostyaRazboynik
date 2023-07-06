package com.konstantinmuzhik.hw1todoapp.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.konstantinmuzhik.hw1todoapp.data.models.Priority

@Entity(tableName = "todo_items")
data class ToDoItemEntity(
    @PrimaryKey @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "description") var title: String,
    @ColumnInfo(name = "importance") var priority: Priority,
    @ColumnInfo(name = "deadline") var deadline: Long?,
    @ColumnInfo(name = "done") var done: Boolean,
    @ColumnInfo(name = "createdAt") val createdAt: Long,
    @ColumnInfo(name = "changedAt") var changedAt: Long?
)