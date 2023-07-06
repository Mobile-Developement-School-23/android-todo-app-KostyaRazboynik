package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthSdk

class AuthYandexController(
    private val context: Context?,
    private val requestCode: Int,
    private val resultCode: Int,
    private val data: Intent?,
    private val navController: NavController,
    private val sharedPreferencesAppSettings: SharedPreferencesAppSettings,
    private val sdk: YandexAuthSdk,
    private val viewModel: ToDoItemViewModel
) {

    private val REQUEST_LOGIN_SDK = 1

    fun setUpYandexResult() {
        if (requestCode == REQUEST_LOGIN_SDK) {
            try {
                val yandexAuthToken = sdk.extractToken(resultCode, data)
                if (yandexAuthToken != null) {
                    val curToken = yandexAuthToken.value
                    if (curToken != sharedPreferencesAppSettings.getCurrentToken()) {
                        viewModel.deleteAllToDoItems()
                        sharedPreferencesAppSettings.setCurrentToken("OAuth ${yandexAuthToken.value}")
                        sharedPreferencesAppSettings.putRevisionId(0)
                    }
                    sharedPreferencesAppSettings.setCurrentToken("OAuth ${yandexAuthToken.value}")
                    navController.navigate(R.id.action_fragmentAuth_to_listFragment)
                }
            } catch (e: YandexAuthException) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
            return
        }
    }
}