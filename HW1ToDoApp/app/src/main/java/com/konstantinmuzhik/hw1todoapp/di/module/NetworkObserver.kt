package com.konstantinmuzhik.hw1todoapp.di.module

import android.content.Context
import com.konstantinmuzhik.hw1todoapp.data.repository.internet_checker.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module for Network Observer
 *
 * @author Kovalev Konstantin
 *
 */
@Module
object NetworkObserver {
    @Provides
    @Singleton
    fun provideConnectivityObserver(context: Context): NetworkConnectivityObserver =
        NetworkConnectivityObserver(context)
}