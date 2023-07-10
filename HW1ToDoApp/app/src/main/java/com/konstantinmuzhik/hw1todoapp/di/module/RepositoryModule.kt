package com.konstantinmuzhik.hw1todoapp.di.module

import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.dao.ToDoItemDao
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.ToDoItemApi
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module for ToDoItem Repository
 *
 * @author Kovalev Konstantin
 *
 */
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        localDataSource: ToDoItemDao,
        remoteDataSource: ToDoItemApi,
        sharedPreferences: SharedPreferencesAppSettings
    ): ToDoItemsRepositoryImpl =
        ToDoItemsRepositoryImpl(localDataSource, remoteDataSource, sharedPreferences)
}