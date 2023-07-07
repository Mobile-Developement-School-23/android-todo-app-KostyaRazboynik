package com.konstantinmuzhik.hw1todoapp.di.module

import com.konstantinmuzhik.hw1todoapp.data.yandex_passport.YandexPassportApi
import com.konstantinmuzhik.hw1todoapp.utils.Constants.YANDEX_BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object YandexPassportApiModule {

    @Provides
    fun providePassportRetrofitService(): YandexPassportApi =
        Retrofit.Builder().baseUrl(YANDEX_BASE_URL).client(OkHttpClient.Builder().build())
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build().create(YandexPassportApi::class.java)
}