package com.konstantinmuzhik.hw1todoapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Calendar
import java.util.Date

@Parcelize
data class ToDoItem(
    var id: String,
    var title: String,
    var priority: Priority,
    var deadline: Date?,
    var done: Boolean,
    val createdAt: Date,
    var changedAt: Date?
) : Parcelable {

    constructor() : this(
        "-1",
        "",
        Priority.NO,
        null,
        false,
        Calendar.getInstance().time,
        Calendar.getInstance().time
    )

    fun setArgs(
        title: String,
        priority: Priority,
        deadline: Date?,
        done: Boolean,
        changeDate: Date?
    ) {
        this.title = title
        this.priority = priority
        this.deadline = deadline
        this.done = done
        this.changedAt = changeDate
    }
}