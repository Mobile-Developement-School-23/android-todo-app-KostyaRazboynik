package com.kostyarazboynik.todoapp.di.module.network

import com.kostyarazboynik.todoapp.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
object NetworkModule {

    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(Constants.RETROFIT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.RETROFIT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.RETROFIT_TIMEOUT, TimeUnit.SECONDS)
            .build()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
}