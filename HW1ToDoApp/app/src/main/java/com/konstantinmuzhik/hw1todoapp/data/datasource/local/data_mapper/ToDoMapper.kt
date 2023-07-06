package com.konstantinmuzhik.hw1todoapp.data.datasource.local.data_mapper

import com.konstantinmuzhik.hw1todoapp.data.datasource.local.entity.ToDoItemEntity
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import java.util.Date

object ToDoMapper {

    fun modelToEntity(toDoItem: ToDoItem): ToDoItemEntity =
        ToDoItemEntity(
            id = toDoItem.id,
            title = toDoItem.title,
            priority = toDoItem.priority,
            deadline = toDoItem.deadline?.time,
            done = toDoItem.done,
            createdAt = toDoItem.createdAt.time,
            changedAt = toDoItem.changedAt?.time
        )

    fun entityToModel(todoItemEntity: ToDoItemEntity): ToDoItem =
        ToDoItem(
            id = todoItemEntity.id,
            title = todoItemEntity.title,
            priority = todoItemEntity.priority,
            deadline = todoItemEntity.deadline?.let { Date(it) },
            done = todoItemEntity.done,
            createdAt = Date(todoItemEntity.createdAt),
            changedAt = todoItemEntity.changedAt?.let { Date(it) }
        )
}