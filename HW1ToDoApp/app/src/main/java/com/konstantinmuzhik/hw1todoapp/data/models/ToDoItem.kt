package com.konstantinmuzhik.hw1todoapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * ToDoItem data model
 *
 * @author Konstantin Kovalev
 *
 */
@Parcelize
data class ToDoItem(
    var id: String,
    var title: String,
    var priority: Priority,
    var deadline: Date? = null,
    var done: Boolean,
    var createdAt: Date,
    var changedAt: Date? = null
) : Parcelable {

    constructor() : this("-1", "", Priority.NO, null, false, Date(), Date())

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