package com.kostyarazboynik.todoapp.di.module.network

import com.kostyarazboynik.todoapp.data.datasource.yandex_passport.YandexPassportApi
import com.kostyarazboynik.todoapp.di.customscope.AppScope
import com.kostyarazboynik.todoapp.di.qualifiers.YandexPassport
import com.kostyarazboynik.todoapp.utils.Constants.YANDEX_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object YandexPassportNetworkModule {

    @Reusable
    @Provides
    fun provideYandexPassportApi(
        @YandexPassport
        retrofitClient: Retrofit
    ): YandexPassportApi = retrofitClient.create(YandexPassportApi::class.java)


    @AppScope
    @Provides
    @YandexPassport
    fun provideYandexPassportRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(YANDEX_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

}