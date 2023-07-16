package com.konstantinmuzhik.hw1todoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepositoryImpl
import com.konstantinmuzhik.hw1todoapp.data.repository.YandexPassportRepository
import com.konstantinmuzhik.hw1todoapp.data.repository.internet_checker.NetworkConnectivityObserver
import com.konstantinmuzhik.hw1todoapp.domain.notifications.NotificationsSchedulerImpl
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose.ToDoItemViewModelCompose
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth.AuthViewModel
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.main.MainViewModel
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings.YandexAuthViewModel
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
    private val schedulerImpl: NotificationsSchedulerImpl
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java ->
                MainViewModel(repositoryImpl, connectivityObserver, coroutineScope)

            AuthViewModel::class.java -> AuthViewModel(repositoryImpl, coroutineScope, schedulerImpl)

            ToDoItemViewModel::class.java -> ToDoItemViewModel(repositoryImpl, coroutineScope)

            YandexAuthViewModel::class.java -> YandexAuthViewModel(yandexPassportRepository)

            ToDoItemViewModelCompose::class.java -> ToDoItemViewModelCompose(repositoryImpl, coroutineScope)

            else -> error("Unknown view model class")
        } as T
}