package com.kostyarazboynik.todoapp.data.models

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
    var importance: Importance,
    var deadline: Date? = null,
    var done: Boolean,
    var createdAt: Date,
    var changedAt: Date? = null
) : Parcelable {

    constructor() : this("", "", Importance.NO, null, false, Date(0), null)

    fun setArgs(
        title: String,
        importance: Importance,
        deadline: Date?,
        done: Boolean,
        changeDate: Date?
    ) {
        this.title = title
        this.importance = importance
        this.deadline = deadline
        this.done = done
        this.changedAt = changeDate
    }
}