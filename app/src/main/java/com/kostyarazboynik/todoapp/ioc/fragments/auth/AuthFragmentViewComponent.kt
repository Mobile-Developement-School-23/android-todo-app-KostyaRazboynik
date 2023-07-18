package com.kostyarazboynik.todoapp.ioc.fragments.auth

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.kostyarazboynik.todoapp.databinding.FragmentAuthBinding
import com.kostyarazboynik.todoapp.ui.view.fragments.auth.AuthViewController
import com.kostyarazboynik.todoapp.ui.view.fragments.auth.AuthViewModel
import com.yandex.authsdk.YandexAuthSdk

class AuthFragmentViewComponent (
    binding: FragmentAuthBinding,
    navController: NavController,
    viewModel: AuthViewModel,
    fragment: Fragment,
    yandexAuthSdk: YandexAuthSdk,
    context: Context
) {

    val authViewController = AuthViewController(
        binding = binding,
        navController = navController,
        yandexAuthSdk = yandexAuthSdk,
        viewModel = viewModel,
        fragment = fragment,
        context = context
    )
}