package com.konstantinmuzhik.hw1todoapp.di.module

import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.ToDoItemApi
import com.konstantinmuzhik.hw1todoapp.di.customscope.AppScope
import com.konstantinmuzhik.hw1todoapp.utils.Constants.BASE_URL
import com.konstantinmuzhik.hw1todoapp.utils.Constants.RETROFIT_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
object NetworkModule {

    @AppScope
    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Reusable
    @Provides
    fun provideTodoApiService(retrofitClient: Retrofit): ToDoItemApi =
        retrofitClient.create(ToDoItemApi::class.java)

    @AppScope
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
            .build()

    @AppScope
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

}
