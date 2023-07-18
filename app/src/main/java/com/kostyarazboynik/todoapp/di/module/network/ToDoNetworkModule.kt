package com.kostyarazboynik.todoapp.di.module.network

import com.kostyarazboynik.todoapp.data.datasource.remote.ToDoItemApi
import com.kostyarazboynik.todoapp.di.customscope.AppScope
import com.kostyarazboynik.todoapp.di.qualifiers.ToDo
import com.kostyarazboynik.todoapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object ToDoNetworkModule {

    @Reusable
    @Provides
    fun provideToDoItemApi(
        @ToDo
        retrofitClient: Retrofit
    ): ToDoItemApi = retrofitClient.create(ToDoItemApi::class.java)


    @AppScope
    @Provides
    @ToDo
    fun provideToDoRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
}
