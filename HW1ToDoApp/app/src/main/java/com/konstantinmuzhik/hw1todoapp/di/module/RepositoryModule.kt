package com.konstantinmuzhik.hw1todoapp.di.module

import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepositoryImpl
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.ToDoItemDatabase
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.ToDoItemApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        localDataSource: ToDoItemDatabase,
        remoteDataSource: ToDoItemApi,
        sharedPreferences: SharedPreferencesAppSettings
    ): ToDoItemsRepositoryImpl =
        ToDoItemsRepositoryImpl(localDataSource, remoteDataSource, sharedPreferences)
}