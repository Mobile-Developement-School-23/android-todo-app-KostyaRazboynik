package com.konstantinmuzhik.hw1todoapp.ioc.auth

import android.view.View
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth.AuthViewController
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.yandex.authsdk.YandexAuthSdk

class AuthFragmentViewComponent (
    root: View,
    navController: NavController,
    sharedPreferencesAppSettings: SharedPreferencesAppSettings,
    sdk: YandexAuthSdk,
    fragmentComponent: AuthFragmentComponent,
    viewModel: ToDoItemViewModel
) {

    val authViewController = AuthViewController(
        root,
        navController,
        sharedPreferencesAppSettings,
        sdk,
        fragmentComponent,
        viewModel
    )
}