package com.kostyarazboynik.todoapp.data.datasource.yandex_passport.entities

import com.google.gson.annotations.SerializedName

/**
 * Info Response for Yandex Passport
 *
 * @author Kovalev Konstantin
 *
 */
data class InfoResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("client_id")
    val clientID: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("real_name")
    val realName: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("sex")
    val sex: String,
    @SerializedName("psuid")
    val psuid: String,
)