package com.konstantinmuzhik.hw1todoapp.di.module

import android.content.Context
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object NetworkObserver {
    @Provides
    @Singleton
    fun provideConnectivityObserver(context: Context): NetworkConnectivityObserver =
        NetworkConnectivityObserver(context)
}