package com.konstantinmuzhik.hw1todoapp.data.repository

import com.konstantinmuzhik.hw1todoapp.data.yandex_passport.YandexPassportApi
import com.konstantinmuzhik.hw1todoapp.data.yandex_passport.entities.InfoResponse
import javax.inject.Inject

class YandexPassportRepository @Inject constructor(
    private val retrofitService: YandexPassportApi,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {
    suspend fun getInfo(): InfoResponse?{
        val value = sharedPreferencesRepository.getAuthToken()
        if (value != null) {
            return try {
                val result = retrofitService.getInfo(value)
                println(result)
                result.body()
            } catch (_:Exception){
                null
            }
        }
        return null
    }
}