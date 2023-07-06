package com.konstantinmuzhik.hw1todoapp.di

import android.content.Context
import android.content.SharedPreferences
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.ToDoItemDatabase
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.RetrofitToDoSource
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.ToDoItemApi
import com.konstantinmuzhik.hw1todoapp.data.yandex_passport.RetrofitYandexSource
import com.konstantinmuzhik.hw1todoapp.data.yandex_passport.YandexPassportApi
import com.konstantinmuzhik.hw1todoapp.di.scope.AppScope
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class NetworkModule {

    @Provides
    @Inject
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

    @Provides
    @AppScope
    fun provideToDoItemRetrofitService(): ToDoItemApi =
        RetrofitToDoSource().makeRetrofitService()

    @Provides
    @AppScope
    fun providePassportRetrofitService(): YandexPassportApi =
        RetrofitYandexSource().makeRetrofitService()

    @Provides
    @Inject
    fun provideRoomDatabase(context: Context): ToDoItemDatabase =
        ToDoItemDatabase.getDatabase(context)

    @Provides
    @Inject
    fun provideSharedPreferencesAppSettings(context: Context): SharedPreferencesAppSettings =
        SharedPreferencesAppSettings(context)

    @Provides
    @Inject
    fun provideNetworkConnectivityObserver(context: Context): NetworkConnectivityObserver =
        NetworkConnectivityObserver(context)
}