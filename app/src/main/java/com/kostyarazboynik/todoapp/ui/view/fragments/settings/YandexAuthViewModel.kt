package com.kostyarazboynik.todoapp.ui.view.fragments.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostyarazboynik.todoapp.data.datasource.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.data.repository.ToDoItemsRepositoryImpl
import com.kostyarazboynik.todoapp.data.repository.YandexPassportRepository
import com.kostyarazboynik.todoapp.domain.models.UiState
import com.kostyarazboynik.todoapp.domain.notifications.NotificationMode
import com.kostyarazboynik.todoapp.utils.Mappers.toOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View Model for Yandex Passport Repository
 *
 * @author Kovalev Konstantin
 *
 */
class YandexAuthViewModel @Inject constructor(
    private val yandexPassportRepository: YandexPassportRepository,
    private val repository: ToDoItemsRepositoryImpl,
    private val sharedPreferences: SharedPreferencesAppSettings,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _accountInfo = MutableStateFlow<String?>(null)
    val accountInfo: StateFlow<String?> = _accountInfo

    init {
        getInfo()
    }

    private fun getInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val value = yandexPassportRepository.getInfo()?.login
            _accountInfo.value = value
        }
    }

    fun setNotificationPermission(permission: Boolean) =
        sharedPreferences.setNotificationPermission(permission)

    fun getNotificationPermission(): Boolean =
        sharedPreferences.getNotificationPermission()

    fun setTheme(theme: Int) = sharedPreferences.setTheme(theme)

    fun getTheme(): Int = sharedPreferences.getTheme()

    fun getOption(context: Context): String =
        sharedPreferences.getNotificationOption().toOption(context)

    fun putThemeMode(mode: String) {
        sharedPreferences.setNotificationOption(NotificationMode.valueOf(mode.uppercase()))
    }

    fun getLabel(): NotificationMode = sharedPreferences.getNotificationOption()
    fun updateNotificationIntents() =
        coroutineScope.launch(Dispatchers.IO) {
            repository.getToDoItemsFlow().collect { uiState ->
                when (uiState) {
                    is UiState.Success -> repository.updateNotifications(uiState.data)
                    else -> {}
                }
            }
        }
}