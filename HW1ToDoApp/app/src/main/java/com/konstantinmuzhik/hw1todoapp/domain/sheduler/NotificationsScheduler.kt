package com.konstantinmuzhik.hw1todoapp.domain.sheduler

import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem

interface NotificationsScheduler {
    fun schedule(item: ToDoItem)
    fun cancel(id: String)
    fun cancelAll()
}