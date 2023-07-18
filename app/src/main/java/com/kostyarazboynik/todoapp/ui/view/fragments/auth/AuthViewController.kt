package com.kostyarazboynik.todoapp.ui.view.fragments.auth

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
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.databinding.FragmentAuthBinding
import com.kostyarazboynik.todoapp.utils.Constants.REQUEST_LOGIN_SDK_CODE
import com.kostyarazboynik.todoapp.utils.Constants.TOKEN_API
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk

/**
 * Auth Fragment View Controller
 *
 * @author Kovalev Konstantin
 *
 */
class AuthViewController(
    private val binding: FragmentAuthBinding,
    private val navController: NavController,
    private val yandexAuthSdk: YandexAuthSdk,
    private val viewModel: AuthViewModel,
    private val fragment: Fragment,
    private val context: Context,
) {

    private lateinit var register: ActivityResultLauncher<Intent>

    fun setUpViews() {
        setUpPermission()
        setUpRegister()
        setUpYandexAuthButton()
        setUpLogInButton()
    }

    private val notificationPermissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            viewModel.setNotificationPermission(isGranted)
        }

    private fun setUpPermission() {
        if (!viewModel.getNotificationPermission()) {
            if (Build.VERSION.SDK_INT >= 33)
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            else showSettingDialog()
        }
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
            if (viewModel.getToken() != TOKEN_API) {
                viewModel.setToken(TOKEN_API)
                viewModel.putRevisionId(0)
                viewModel.deleteCurrentItems()
            }
            navController.navigate(R.id.action_fragmentAuth_to_listFragment)
        }

    private fun setUpYandexAuthButton() =
        binding.yandexAuthButton.setOnClickListener {
            register.launch(yandexAuthSdk.createLoginIntent(YandexAuthLoginOptions.Builder().build()))
        }


    private fun setUpToken(result: ActivityResult, data: Intent?) {
        val yandexAuthToken = yandexAuthSdk.extractToken(result.resultCode, data)
        if (yandexAuthToken != null) {
            val curToken = yandexAuthToken.value
            if (curToken != viewModel.getToken()) {
                viewModel.setToken(curToken)
                viewModel.putRevisionId(0)
                viewModel.deleteCurrentItems()
            }
            navController.navigate(R.id.action_fragmentAuth_to_listFragment)
        }
    }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            ContextThemeWrapper(fragment.context, R.style.AlertDialogCustom)
        )
            .setTitle(context.getString(R.string.permission_title))
            .setMessage(context.getString(R.string.permission_message))
            .setPositiveButton(context.getString(R.string.permission_yes)) { _, _ ->
                viewModel.setNotificationPermission(true)
            }
            .setNegativeButton(context.getString(R.string.permission_no)) { _, _ ->
                viewModel.setNotificationPermission(false)
            }
            .show()
    }
}