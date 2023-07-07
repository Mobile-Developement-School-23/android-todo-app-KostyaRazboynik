package com.konstantinmuzhik.hw1todoapp.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.dao.ToDoItemDao
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.entity.ToDoItemEntity

@Database(entities = [ToDoItemEntity::class], version = 1)
abstract class ToDoItemDatabase : RoomDatabase() {

    abstract fun toDoItemDao(): ToDoItemDao
}