package com.kostyarazboynik.todoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kostyarazboynik.todoapp.data.datasource.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.data.repository.ToDoItemsRepositoryImpl
import com.kostyarazboynik.todoapp.data.repository.YandexPassportRepository
import com.kostyarazboynik.todoapp.data.repository.internet_checker.NetworkConnectivityObserver
import com.kostyarazboynik.todoapp.domain.notifications.NotificationsSchedulerImpl
import com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.ToDoItemViewModelCompose
import com.kostyarazboynik.todoapp.ui.view.fragments.auth.AuthViewModel
import com.kostyarazboynik.todoapp.ui.view.fragments.main.MainViewModel
import com.kostyarazboynik.todoapp.ui.view.fragments.settings.YandexAuthViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * View Model Factory
 *
 * @author Kovalev Konstantin
 *
 */
class ViewModelFactory @Inject constructor(
    private val repositoryImpl: ToDoItemsRepositoryImpl,
    private val connectivityObserver: NetworkConnectivityObserver,
    private val coroutineScope: CoroutineScope,
    private val yandexPassportRepository: YandexPassportRepository,
    private val sharedPreferences: SharedPreferencesAppSettings,
    private val schedulerImpl: NotificationsSchedulerImpl
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java ->
                MainViewModel(repositoryImpl, connectivityObserver, coroutineScope)

            AuthViewModel::class.java -> AuthViewModel(repositoryImpl, coroutineScope, sharedPreferences, schedulerImpl)

            ToDoItemViewModel::class.java -> ToDoItemViewModel(repositoryImpl, coroutineScope)

            YandexAuthViewModel::class.java -> YandexAuthViewModel(yandexPassportRepository,repositoryImpl, sharedPreferences, coroutineScope)

            ToDoItemViewModelCompose::class.java -> ToDoItemViewModelCompose(repositoryImpl, coroutineScope)

            else -> error("Unknown view model class")
        } as T
}