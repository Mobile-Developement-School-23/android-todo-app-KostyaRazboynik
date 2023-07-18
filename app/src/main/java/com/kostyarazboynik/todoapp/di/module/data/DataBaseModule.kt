package com.kostyarazboynik.todoapp.di.module.data

import android.content.Context
import androidx.room.Room
import com.kostyarazboynik.todoapp.data.datasource.local.ToDoItemDatabase
import com.kostyarazboynik.todoapp.data.datasource.local.dao.ToDoItemDao
import com.kostyarazboynik.todoapp.di.customscope.AppScope
import com.kostyarazboynik.todoapp.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object DataBaseModule {

    @Reusable
    @Provides
    fun provideToDoDao(database: ToDoItemDatabase): ToDoItemDao = database.toDoItemDao()

    @AppScope
    @Provides
    fun provideDataBase(context: Context): ToDoItemDatabase =
        Room.databaseBuilder(context, ToDoItemDatabase::class.java, DATABASE_NAME).build()

}
