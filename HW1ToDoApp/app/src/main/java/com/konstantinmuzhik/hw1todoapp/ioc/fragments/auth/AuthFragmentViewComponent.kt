package com.konstantinmuzhik.hw1todoapp.ioc.fragments.auth

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAuthBinding
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth.AuthViewController
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth.AuthViewModel
import com.yandex.authsdk.YandexAuthSdk

class AuthFragmentViewComponent (
    binding: FragmentAuthBinding,
    navController: NavController,
    sharedPreferencesRepository: SharedPreferencesAppSettings,
    viewModel: AuthViewModel,
    fragment: Fragment,
    sdk: YandexAuthSdk,
    context: Context
) {

    val authViewController = AuthViewController(
        binding,
        navController,
        sharedPreferencesRepository,
        sdk,
        viewModel,
        fragment,
        context
    )
}