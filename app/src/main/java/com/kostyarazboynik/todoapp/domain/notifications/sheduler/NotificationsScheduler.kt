package com.kostyarazboynik.todoapp.domain.notifications.sheduler

import com.kostyarazboynik.todoapp.data.models.ToDoItem

interface NotificationsScheduler {
    fun schedule(item: ToDoItem)
    fun cancel(id: String)
    fun cancelAll()
}