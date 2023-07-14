package com.konstantinmuzhik.hw1todoapp.di.module

import android.content.Context
import com.konstantinmuzhik.hw1todoapp.data.repository.internet_checker.NetworkConnectivityObserver
import com.konstantinmuzhik.hw1todoapp.di.customscope.AppScope
import dagger.Module
import dagger.Provides

@Module
object NetworkObserver {

    @AppScope
    @Provides
    fun provideConnectivityObserver(context: Context): NetworkConnectivityObserver =
        NetworkConnectivityObserver(context)
}
