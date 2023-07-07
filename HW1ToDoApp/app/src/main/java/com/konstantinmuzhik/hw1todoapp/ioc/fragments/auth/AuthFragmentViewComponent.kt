package com.konstantinmuzhik.hw1todoapp.ioc.fragments.auth

import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAuthBinding
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth.AuthViewController
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.yandex.authsdk.YandexAuthSdk

class AuthFragmentViewComponent (
    binding: FragmentAuthBinding,
    navController: NavController,
    sharedPreferencesRepository: SharedPreferencesAppSettings,
    sdk: YandexAuthSdk,
    fragmentComponent: AuthFragmentComponent,
    mToDoItemViewModel: ToDoItemViewModel
) {

    val authViewController = AuthViewController(
        binding,
        navController,
        sharedPreferencesRepository,
        sdk,
        fragmentComponent,
        mToDoItemViewModel
    )
}