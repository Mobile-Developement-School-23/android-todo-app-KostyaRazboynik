package com.konstantinmuzhik.hw1todoapp.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepositoryImpl
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.data.repository.internet_checker.NetworkConnectivityObserver
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
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            ToDoItemViewModel::class.java -> ToDoItemViewModel(repositoryImpl, connectivityObserver)

            else -> throw IllegalStateException("Unknown view model class")
        }
        return viewModel as T
    }
}