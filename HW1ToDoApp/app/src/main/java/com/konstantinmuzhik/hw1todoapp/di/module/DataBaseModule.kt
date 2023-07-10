package com.konstantinmuzhik.hw1todoapp.di.module

import android.content.Context
import androidx.room.Room
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.ToDoItemDatabase
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.dao.ToDoItemDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Database Module
 *
 * @author Kovalev Konstantin
 *
 */
@Module
object DataBaseModule {

    @Singleton
    @Provides
    fun provideToDoDao(database: ToDoItemDatabase): ToDoItemDao =
        database.toDoItemDao()

    @Singleton
    @Provides
    fun provideDataBase(context: Context): ToDoItemDatabase =
        Room.databaseBuilder(context, ToDoItemDatabase::class.java, "main_database").build()
}