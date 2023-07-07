package com.konstantinmuzhik.hw1todoapp.ioc.auth

import android.content.Context
import android.content.Intent
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.yandex.authsdk.YandexAuthSdk

class AuthFragmentYandexComponent(
    context: Context?,
    requestCode: Int,
    resultCode: Int,
    data: Intent?,
    navController: NavController,
    sharedPreferencesRepository: SharedPreferencesAppSettings,
    sdk: YandexAuthSdk,
    viewModel: ToDoItemViewModel
) {

//    val authYandexController = AuthYandexController(
//        context,
//        requestCode,
//        resultCode,
//        data,
//        navController,
//        sharedPreferencesRepository,
//        sdk,
//        viewModel
//    )
}