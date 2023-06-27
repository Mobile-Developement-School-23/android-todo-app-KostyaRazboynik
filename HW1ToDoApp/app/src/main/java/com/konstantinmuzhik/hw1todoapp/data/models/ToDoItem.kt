package com.konstantinmuzhik.hw1todoapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(tableName = "todo_table")
@Parcelize
@TypeConverters(Converter::class)
data class ToDoItem(
    @PrimaryKey
    val id: String,
    val title: String,
    val priority: Priority,
    val deadline: Date?,
    var done: Boolean,
    val creationDate: Date,
    val changeDate: Date?
) : Parcelable