package com.kostyarazboynik.todoapp.di.module.network

import android.content.Context
import com.kostyarazboynik.todoapp.data.repository.internet_checker.NetworkConnectivityObserver
import com.kostyarazboynik.todoapp.di.customscope.AppScope
import dagger.Module
import dagger.Provides

@Module
object NetworkObserver {

    @AppScope
    @Provides
    fun provideConnectivityObserver(context: Context): NetworkConnectivityObserver =
        NetworkConnectivityObserver(context)
}
