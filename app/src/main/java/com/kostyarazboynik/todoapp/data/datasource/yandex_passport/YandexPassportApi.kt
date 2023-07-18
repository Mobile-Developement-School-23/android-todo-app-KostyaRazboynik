package com.kostyarazboynik.todoapp.data.datasource.yandex_passport

import com.kostyarazboynik.todoapp.data.datasource.yandex_passport.entities.InfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Api for Yandex Passport
 *
 * @author Kovalev Konstantin
 *
 */
interface YandexPassportApi {

    @GET("info")
    suspend fun getInfo(
        @Header("Authorization")
        auth: String
    ): Response<InfoResponse>
}