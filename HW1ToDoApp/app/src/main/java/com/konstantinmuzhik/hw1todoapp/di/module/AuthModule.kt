package com.konstantinmuzhik.hw1todoapp.di.module

import android.content.Context
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Module
import dagger.Provides

@Module
object AuthModule {

    @Provides
    fun provideYandexAuthSdk(context: Context): YandexAuthSdk =
        YandexAuthSdk(context, YandexAuthOptions(context))
}