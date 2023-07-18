package com.kostyarazboynik.todoapp.ui.view.fragments.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.kostyarazboynik.todoapp.data.datasource.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.data.repository.ToDoItemsRepositoryImpl
import com.kostyarazboynik.todoapp.domain.notifications.NotificationsSchedulerImpl
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val repository: ToDoItemsRepositoryImpl,
    private val coroutineScope: CoroutineScope,
    private val sharedPreferences: SharedPreferencesAppSettings,
    private val schedulerImpl: NotificationsSchedulerImpl
) : ViewModel() {

    fun deleteCurrentItems() {
        schedulerImpl.cancelAll()
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteCurrentToDoItems()
        }
    }

    fun setToken(token: String) = sharedPreferences.setCurrentToken(token)

    fun getToken(): String = sharedPreferences.getCurrentToken()

    fun setNotificationPermission(permission: Boolean) =
        sharedPreferences.setNotificationPermission(permission)

    fun getNotificationPermission(): Boolean =
        sharedPreferences.getNotificationPermission()

    fun putRevisionId(id: Int) = sharedPreferences.putRevisionId(id)
}
