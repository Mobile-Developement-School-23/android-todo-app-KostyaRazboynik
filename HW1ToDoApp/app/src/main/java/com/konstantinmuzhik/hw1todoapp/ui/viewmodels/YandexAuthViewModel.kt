package com.konstantinmuzhik.hw1todoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepositoryImpl
import com.konstantinmuzhik.hw1todoapp.data.repository.YandexPassportRepository
import com.konstantinmuzhik.hw1todoapp.utils.Constants.SHARED_PREFERENCES_NO_TOKEN
import com.yandex.authsdk.YandexAuthToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class YandexAuthViewModel @Inject constructor(
    private val yandexPassportRepository: YandexPassportRepository
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
}