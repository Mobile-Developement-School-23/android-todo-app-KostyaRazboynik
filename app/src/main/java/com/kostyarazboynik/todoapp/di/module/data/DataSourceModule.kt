package com.kostyarazboynik.todoapp.di.module.data

import android.content.Context
import com.kostyarazboynik.todoapp.data.datasource.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.data.datasource.remote.ToDoItemApi
import com.kostyarazboynik.todoapp.data.repository.RemoteToDoItemsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object DataSourceModule {

    @Reusable
    @Provides
    fun provideNetworkSource(
        sharedPreferences: SharedPreferencesAppSettings,
        retrofitService: ToDoItemApi
    ): RemoteToDoItemsRepositoryImpl =
        RemoteToDoItemsRepositoryImpl(sharedPreferences, retrofitService)

    @Reusable
    @Provides
    fun provideSharedPreferencesDataSource(context: Context): SharedPreferencesAppSettings =
        SharedPreferencesAppSettings(context)
}
