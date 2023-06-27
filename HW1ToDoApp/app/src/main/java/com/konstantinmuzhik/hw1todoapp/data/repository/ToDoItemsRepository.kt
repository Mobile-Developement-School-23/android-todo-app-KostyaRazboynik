package com.konstantinmuzhik.hw1todoapp.data.repository

import androidx.lifecycle.LiveData
import com.konstantinmuzhik.hw1todoapp.data.dao.ToDoItemDAO
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem

class ToDoItemsRepository(private val toDoItemDao: ToDoItemDAO) {

    val getAllData: LiveData<List<ToDoItem>> = toDoItemDao.getAllData()

    fun insertData(toDoItem: ToDoItem) = toDoItemDao.insertData(toDoItem)

    fun updateData(toDoItem: ToDoItem) = toDoItemDao.updateData(toDoItem)

    fun deleteItem(toDoItem: ToDoItem) = toDoItemDao.deleteItem(toDoItem)
}
