package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konstantinmuzhik.hw1todoapp.data.repository.YandexPassportRepository
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