package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.ContextThemeWrapper
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAuthBinding
import com.konstantinmuzhik.hw1todoapp.utils.Constants.REQUEST_LOGIN_SDK_CODE
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHARED_PREFERENCES_NO_TOKEN
import com.konstantinmuzhik.hw1todoapp.utils.Constants.TOKEN_API
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
    private val viewModel: AuthViewModel,
    private val fragment: Fragment,
    private val context: Context
) {

    private lateinit var register: ActivityResultLauncher<Intent>


    fun setUpViews() {
        setUpPermission()
        deleteToken()
        setUpRegister()
        setUpYandexAuthButton()
        setUpLogInButton()
    }

    private val notificationPermissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            sharedPreferences.setNotificationPermission(isGranted)
        }

    private fun setUpPermission() {
        if(!sharedPreferences.getNotificationPermission()) {
            if (Build.VERSION.SDK_INT >= 33) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                showSettingDialog()
            }
        }
    }

    private fun deleteToken() {
        viewModel.deleteCurrentItems()
        sharedPreferences.setCurrentToken(SHARED_PREFERENCES_NO_TOKEN)
    }

    private fun setUpRegister() {
        register =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == REQUEST_LOGIN_SDK_CODE) {
                    val data: Intent? = result.data
                    if (data != null) {
                        try {
                            setUpToken(result, data)
                        } catch (_: YandexAuthException) {
                        }
                    }
                }
            }
    }

    private fun setUpLogInButton() =
        binding.logInGuestButton.setOnClickListener {
            if (sharedPreferences.getCurrentToken() != TOKEN_API) {
                sharedPreferences.setCurrentToken("Bearer $TOKEN_API")
                sharedPreferences.putRevisionId(832)
            }
            navController.navigate(R.id.action_fragmentAuth_to_listFragment)
        }

    private fun setUpYandexAuthButton() =
        binding.yandexAuthButton.setOnClickListener {
            register.launch(sdk.createLoginIntent(YandexAuthLoginOptions.Builder().build()))        }


    private fun setUpToken(result: ActivityResult, data: Intent?) {
        val yandexAuthToken = sdk.extractToken(result.resultCode, data)
        if (yandexAuthToken != null) {
            val curToken = yandexAuthToken.value
            if (curToken != sharedPreferences.getCurrentToken()) {
                sharedPreferences.setCurrentToken("OAuth ${yandexAuthToken.value}")
                sharedPreferences.putRevisionId(932)
            }
            sharedPreferences.setCurrentToken("OAuth ${yandexAuthToken.value}")
            navController.navigate(R.id.action_fragmentAuth_to_listFragment)
        }
    }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            ContextThemeWrapper(
                fragment.context,
                R.style.AlertDialogCustom
            )
        )
            .setTitle(context.getString(R.string.permission_title))
            .setMessage(context.getString(R.string.permission_message))
            .setPositiveButton(context.getString(R.string.permission_yes)) { _, _ ->
                sharedPreferences.setNotificationPermission(true)
            }
            .setNegativeButton(context.getString(R.string.permission_no)) { _, _ ->
                sharedPreferences.setNotificationPermission(false)
            }
            .show()
    }
}