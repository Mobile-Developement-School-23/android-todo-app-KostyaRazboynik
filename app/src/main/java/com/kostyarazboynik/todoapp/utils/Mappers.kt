package com.kostyarazboynik.todoapp.utils

import android.content.Context
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.data.datasource.local.entity.ToDoItemEntity
import com.kostyarazboynik.todoapp.data.datasource.remote.dto.ToDoItemNetworkEntity
import com.kostyarazboynik.todoapp.data.models.Importance
import com.kostyarazboynik.todoapp.data.models.ToDoItem
import java.util.Date

object Mappers {

    // ToDoItem -> ToDoItemEntity
    fun ToDoItem.modelToEntity(): ToDoItemEntity =
        ToDoItemEntity(
            id = this.id,
            title = this.title,
            importance = this.importance,
            deadline = this.deadline?.time,
            done = this.done,
            createdAt = this.createdAt.time,
            changedAt = this.changedAt?.time
        )

    // ToDoItemEntity -> ToDoItem
    fun ToDoItemEntity.entityToModel(): ToDoItem =
        ToDoItem(
            id = this.id,
            title = this.title,
            importance = this.importance,
            deadline = this.deadline?.let { Date(it) },
            done = this.done,
            createdAt = Date(this.createdAt),
            changedAt = this.changedAt?.let { Date(it) }
        )

    // ToDoItem -> ToDoItemNetworkEntity
    fun ToDoItem.modelToNetworkEntity(deviseId: String): ToDoItemNetworkEntity =
        ToDoItemNetworkEntity(
            id = this.id,
            title = this.title,
            importance = when (this.importance) {
                Importance.LOW -> "low"
                Importance.HIGH -> "high"
                Importance.NO -> "no"
            },
            deadline = this.deadline?.time,
            done = this.done,
            createdAt = this.createdAt.time,
            changedAt = this.changedAt?.time ?: 0,
            lastUpdatedBy = deviseId,
            color = null
        )

    // ToDoItemNetworkEntity -> ToDoItem
    fun ToDoItemNetworkEntity.networkEntityToModel(): ToDoItem =
        ToDoItem(
            id = this.id,
            title = this.title,
            importance = when (this.importance) {
                "low" -> Importance.LOW
                "high" -> Importance.HIGH
                else -> Importance.NO
            },
            deadline = this.deadline?.let { Date(it) },
            done = this.done,
            createdAt = Date(this.createdAt),
            changedAt = Date(this.changedAt)
        )

    // Importance -> String
    fun Importance.getString(context: Context): String =
        when(this) {
            Importance.HIGH -> context.getString(R.string.high_priority)
            Importance.LOW -> context.getString(R.string.low_priority)
            Importance.NO -> context.getString(R.string.no_priority)
        }

    // String -> Importance
    fun String.toImportance(context: Context): Importance =
        when(this) {
            context.getString(R.string.high_priority) -> Importance.HIGH
            context.getString(R.string.low_priority) -> Importance.LOW
            else -> Importance.NO
        }
}