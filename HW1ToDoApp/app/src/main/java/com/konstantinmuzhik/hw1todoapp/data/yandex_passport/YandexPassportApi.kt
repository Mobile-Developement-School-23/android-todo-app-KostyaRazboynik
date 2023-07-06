package com.konstantinmuzhik.hw1todoapp.data.yandex_passport

import com.konstantinmuzhik.hw1todoapp.data.yandex_passport.entities.InfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface YandexPassportApi {

    @GET("info")
    suspend fun getInfo(@Header("Authorization") auth:String):Response<InfoResponse>
}