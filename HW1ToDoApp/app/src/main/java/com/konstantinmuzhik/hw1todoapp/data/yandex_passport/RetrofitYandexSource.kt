package com.konstantinmuzhik.hw1todoapp.data.yandex_passport

import com.konstantinmuzhik.hw1todoapp.utils.Constants.YANDEX_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitYandexSource {

    fun makeRetrofitService(): YandexPassportApi =
        Retrofit.Builder()
            .baseUrl(YANDEX_URL)
            .client(makeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YandexPassportApi::class.java)

    private fun makeOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()
}