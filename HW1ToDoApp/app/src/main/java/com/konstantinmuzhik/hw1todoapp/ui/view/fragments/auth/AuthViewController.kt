package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth

import android.content.Intent
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAuthBinding
import com.konstantinmuzhik.hw1todoapp.ioc.fragments.auth.AuthFragmentComponent
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.utils.Constants
import com.konstantinmuzhik.hw1todoapp.utils.Constants.REQUEST_LOGIN_SDK_CODE
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk

/**
 * Auth Fragment View Controller
 *
 * @author Kovalev Konstantin
 *
 */
class AuthViewController (
    private val binding: FragmentAuthBinding,
    private val navController: NavController,
    private val sharedPreferences: SharedPreferencesAppSettings,
    private val sdk: YandexAuthSdk,
    private val fragmentComponent: AuthFragmentComponent,
    private val mToDoItemViewModel: ToDoItemViewModel
) {

    fun setUpViews() {
        setUpYandexAuthButton()
        setUpLogInButton()
    }

    private fun setUpLogInButton() {
        binding.logInGuestButton.setOnClickListener {
            if (sharedPreferences.getCurrentToken() != Constants.TOKEN_API) {
                mToDoItemViewModel.deleteAll()
                sharedPreferences.setCurrentToken("Bearer ${Constants.TOKEN_API}")
                sharedPreferences.putRevisionId(0)
            }
            navController.navigate(R.id.action_fragmentAuth_to_listFragment)
        }
    }

    private fun setUpYandexAuthButton() {
        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
        val intent = sdk.createLoginIntent(loginOptionsBuilder.build())
        binding.yandexAuthButton.setOnClickListener{
            fragmentComponent.fragment.startActivityForResult(intent, REQUEST_LOGIN_SDK_CODE)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOGIN_SDK_CODE) {
            try {
                setUpToken(resultCode, data)
            } catch (_: YandexAuthException) {
            }
            return
        }
    }

    private fun setUpToken(resultCode: Int, data: Intent?) {
        val yandexAuthToken = sdk.extractToken(resultCode, data)
        if (yandexAuthToken != null) {
            val curToken = yandexAuthToken.value
            if (curToken != sharedPreferences.getCurrentToken()) {
                mToDoItemViewModel.deleteAll()
                sharedPreferences.setCurrentToken("OAuth ${yandexAuthToken.value}")
                sharedPreferences.putRevisionId(0)
            }
            sharedPreferences.setCurrentToken("OAuth ${yandexAuthToken.value}")
            navController.navigate(R.id.action_fragmentAuth_to_listFragment)
        }
    }
}