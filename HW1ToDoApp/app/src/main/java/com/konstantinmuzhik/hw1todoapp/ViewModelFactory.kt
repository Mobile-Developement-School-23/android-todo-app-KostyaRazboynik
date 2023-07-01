package com.konstantinmuzhik.hw1todoapp

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepository
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.utils.locale

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            ToDoItemViewModel::class.java -> ToDoItemViewModel(
                ToDoItemsRepository(
                    locale(),
                    locale(),
                    locale()
                ), locale()
            )

            else -> throw IllegalStateException("Unknown view model class")
        } as T
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)