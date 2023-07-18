package com.kostyarazboynik.todoapp.ui.view.fragments.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostyarazboynik.todoapp.data.datasource.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.data.repository.YandexPassportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val sharedPreferences: SharedPreferencesAppSettings
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
}