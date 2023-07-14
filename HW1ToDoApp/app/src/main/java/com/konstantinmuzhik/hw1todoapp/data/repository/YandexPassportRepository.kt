package com.konstantinmuzhik.hw1todoapp.data.repository

import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.yandex_passport.YandexPassportApi
import com.konstantinmuzhik.hw1todoapp.data.yandex_passport.entities.InfoResponse
import javax.inject.Inject

/**
 * Repository for Yandex Passport
 *
 * @author Kovalev Konstantin
 *
 */
class YandexPassportRepository @Inject constructor(
    private val retrofitService: YandexPassportApi,
    private val sharedPreferencesRepository: SharedPreferencesAppSettings,
) {
    suspend fun getInfo(): InfoResponse? =
        try {
            val result = retrofitService.getInfo(sharedPreferencesRepository.getCurrentToken())
            println(result)
            result.body()
        } catch (_: Exception) {
            null
        }

}