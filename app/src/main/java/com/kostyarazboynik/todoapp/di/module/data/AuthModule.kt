package com.kostyarazboynik.todoapp.di.module.data

import android.content.Context
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object AuthModule {
    @Reusable
    @Provides
    fun provideYandexAuthSdk(context: Context): YandexAuthSdk =
        YandexAuthSdk(context, YandexAuthOptions(context, true))

}
