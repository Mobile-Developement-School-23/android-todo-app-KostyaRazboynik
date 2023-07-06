package com.konstantinmuzhik.hw1todoapp.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.dao.ToDoItemDao
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.entity.ToDoItemEntity

@Database(entities = [ToDoItemEntity::class], version = 1)
abstract class ToDoItemDatabase : RoomDatabase() {

    abstract fun toDoItemDao(): ToDoItemDao

    companion object {

        @Volatile
        private var INSTANCE: ToDoItemDatabase? = null

        fun getDatabase(context: Context): ToDoItemDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ToDoItemDatabase::class.java,
                    "main_database"
                ).build()

                INSTANCE = instance

                instance
            }
    }
}