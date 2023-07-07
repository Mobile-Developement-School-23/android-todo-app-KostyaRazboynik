package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Yandex Auth View Model Factory
 *
 * @author Kovalev Konstantin
 *
 */
class YandexAuthViewModelFactory<T : ViewModel>(private val create: () -> T) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return create.invoke() as T
    }
}