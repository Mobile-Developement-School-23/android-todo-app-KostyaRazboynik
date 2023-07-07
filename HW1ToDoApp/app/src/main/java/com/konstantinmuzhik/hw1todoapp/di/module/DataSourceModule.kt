package com.konstantinmuzhik.hw1todoapp.di.module

import android.content.Context
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataSourceModule {

    @Singleton
    @Provides
    fun provideSharedPreferencesDataSource(context: Context): SharedPreferencesAppSettings =
        SharedPreferencesAppSettings(context)

}