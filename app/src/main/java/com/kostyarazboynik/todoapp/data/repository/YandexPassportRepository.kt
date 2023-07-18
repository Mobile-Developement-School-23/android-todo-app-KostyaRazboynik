package com.kostyarazboynik.todoapp.data.repository

import com.kostyarazboynik.todoapp.data.datasource.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.data.datasource.yandex_passport.YandexPassportApi
import com.kostyarazboynik.todoapp.data.datasource.yandex_passport.entities.InfoResponse
import javax.inject.Inject

/**
 * Repository for Yandex Passport
 *
 * @author Kovalev Konstantin
 *
 */
class YandexPassportRepository @Inject constructor(
    private val retrofitService: YandexPassportApi,
    private val sharedPreferences: SharedPreferencesAppSettings,
) {
    suspend fun getInfo(): InfoResponse? =
        try {
            val result = retrofitService.getInfo(sharedPreferences.getFullCurrentToken())
            println(result)
            result.body()
        } catch (_: Exception) {
            null
        }

}