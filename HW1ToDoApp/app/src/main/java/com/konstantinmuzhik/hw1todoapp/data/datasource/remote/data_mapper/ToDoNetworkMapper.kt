package com.konstantinmuzhik.hw1todoapp.data.datasource.remote.data_mapper

import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.dto.ToDoItemNetworkEntity
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import java.util.Date

object ToDoNetworkMapper {

    fun modelToNetworkEntity(toDoItem: ToDoItem, deviseId: String): ToDoItemNetworkEntity =
        ToDoItemNetworkEntity(
            id = toDoItem.id,
            title = toDoItem.title,
            priority = toDoItem.priority,
            deadline = toDoItem.deadline?.time,
            done = toDoItem.done,
            createdAt = toDoItem.createdAt.time,
            changedAt = toDoItem.changedAt?.time ?: 0,
            last_updated_by = deviseId,
            color = null
        )

    fun networkEntityToModel(todoItemEntity: ToDoItemNetworkEntity): ToDoItem =
        ToDoItem(
            id = todoItemEntity.id,
            title = todoItemEntity.title,
            priority = todoItemEntity.priority,
            deadline = todoItemEntity.deadline?.let { Date(it) },
            done = todoItemEntity.done,
            createdAt = Date(todoItemEntity.createdAt),
            changedAt = Date(todoItemEntity.changedAt)
        )
}