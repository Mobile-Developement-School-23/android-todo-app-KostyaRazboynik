package com.konstantinmuzhik.hw1todoapp.di.module

import android.content.Context
import com.konstantinmuzhik.hw1todoapp.data.yandex_passport.YandexPassportApi
import com.konstantinmuzhik.hw1todoapp.utils.Constants
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object AuthModule {
    @Reusable
    @Provides
    fun provideYandexAuthSdk(context: Context): YandexAuthSdk =
        YandexAuthSdk(context, YandexAuthOptions(context, true))

    @Provides
    fun providePassportRetrofitService(): YandexPassportApi =
        Retrofit.Builder().baseUrl(Constants.YANDEX_BASE_URL).client(OkHttpClient.Builder().build())
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build().create(YandexPassportApi::class.java)
}
