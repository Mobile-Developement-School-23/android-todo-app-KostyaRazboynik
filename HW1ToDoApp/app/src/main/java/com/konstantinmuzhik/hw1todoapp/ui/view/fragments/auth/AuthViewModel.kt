package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth

import androidx.lifecycle.ViewModel
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepositoryImpl
import com.konstantinmuzhik.hw1todoapp.ui.notifications.NotificationsSchedulerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val repository: ToDoItemsRepositoryImpl,
    private val coroutineScope: CoroutineScope,
    private val schedulerImpl: NotificationsSchedulerImpl
) : ViewModel() {

    fun deleteCurrentItems() {
        schedulerImpl.cancelAll()
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteCurrentToDoItems()
        }
    }

}
