package com.konstantinmuzhik.hw1todoapp.di.module

import android.content.Context
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.ToDoItemApi
import com.konstantinmuzhik.hw1todoapp.data.repository.RemoteToDoItemsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object DataSourceModule {

    @Reusable
    @Provides
    fun provideNetworkSource(
        sharedPreferencesHelper: SharedPreferencesAppSettings,
        retrofitService: ToDoItemApi
    ): RemoteToDoItemsRepositoryImpl =
        RemoteToDoItemsRepositoryImpl(sharedPreferencesHelper, retrofitService)

    @Reusable
    @Provides
    fun provideSharedPreferencesDataSource(context: Context) =
        SharedPreferencesAppSettings(context)
}
