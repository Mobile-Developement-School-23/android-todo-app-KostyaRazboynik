package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth

import android.view.View
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.ioc.auth.AuthFragmentComponent
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.utils.Constants
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk

class AuthViewController (
    private val rootView: View,
    private val navController: NavController,
    private val sharedPreferencesAppSettings: SharedPreferencesAppSettings,
    private val sdk: YandexAuthSdk,
    private val fragmentComponent: AuthFragmentComponent,
    private val viewModel: ToDoItemViewModel
) {

    private val REQUEST_LOGIN_SDK = 1

    fun setUpViews() {
        setUpYandexAuthButton()
        setUpLogInButton()
    }

    private fun setUpLogInButton() {
        rootView.findViewById<CardView>(R.id.log_in_button).setOnClickListener{
            if (sharedPreferencesAppSettings.getCurrentToken() != Constants.TOKEN_API) {
                viewModel.deleteAllToDoItems()
                sharedPreferencesAppSettings.setCurrentToken("Bearer ${Constants.TOKEN_API}")
                sharedPreferencesAppSettings.putRevisionId(0)
            }
            navController.navigate(R.id.action_fragmentAuth_to_listFragment)
        }
    }

    private fun setUpYandexAuthButton() {
        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
        val intent = sdk.createLoginIntent(loginOptionsBuilder.build())
        rootView.findViewById<CardView>(R.id.yandex_auth_button).setOnClickListener {
            fragmentComponent.fragment.startActivityForResult(intent, REQUEST_LOGIN_SDK)
        }
    }

}