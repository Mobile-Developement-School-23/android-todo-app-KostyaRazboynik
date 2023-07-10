package com.konstantinmuzhik.hw1todoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepositoryImpl
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.domain.models.LoadingState
import com.konstantinmuzhik.hw1todoapp.data.repository.internet_checker.ConnectivityObserver
import com.konstantinmuzhik.hw1todoapp.data.repository.internet_checker.NetworkConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View Model for ToDoItem Repository
 *
 * @author Kovalev Konstantin
 *
 */
class ToDoItemViewModel @Inject constructor(
    private val repository: ToDoItemsRepositoryImpl,
    private val connection: NetworkConnectivityObserver
) : ViewModel() {
    private var job: Job? = null

    var modeAll: Boolean = false

    private val _status = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    val status = _status.asStateFlow()

    private val _tasks = MutableSharedFlow<List<ToDoItem>>()
    val tasks: SharedFlow<List<ToDoItem>> = _tasks.asSharedFlow()

    val countCompletedTask: Flow<Int> = _tasks.map { it -> it.count { it.done } }

    private val _loadingState =
        MutableStateFlow<LoadingState<Any>>(LoadingState.Success("Loaded from rood complete!"))

    private var _currentItem = MutableStateFlow(ToDoItem())
    var currentItem = _currentItem.asStateFlow()

    private val scope = Dispatchers.IO

    init {
        observeNetwork()
        loadLocalData()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            connection.observe().collectLatest {
                _status.emit(it)
            }
        }
    }

    fun changeDone() {
        modeAll = !modeAll
        job?.cancel()
        loadLocalData()
    }

    fun loadLocalData() {
        job = viewModelScope.launch(scope) {
            _tasks.emitAll(repository.getAllToDoItems())
        }
    }

    fun createTask(todoItem: ToDoItem) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.createItem(todoItem)
        }

    fun deleteTask(todoItem: ToDoItem) =
        viewModelScope.launch(scope) {
            repository.deleteToDoItem(todoItem)
        }

    fun updateTask(newItem: ToDoItem) =
        viewModelScope.launch(scope) {
            repository.updateToDoItem(newItem)
        }

    fun changeTaskDone(task: ToDoItem) =
        viewModelScope.launch(scope) {
            repository.updateStatusToDoItem(task.id, !task.done)
        }

    fun loadTask(id: String) =
        viewModelScope.launch(scope) {
            _currentItem.emit(repository.getToDoItemById(id))
        }

    fun loadRemoteList() {
        if (status.value == ConnectivityObserver.Status.Available) {
            _loadingState.value = LoadingState.Loading(true)
            viewModelScope.launch(scope) {
                _loadingState.emit(repository.getRemoteTasks())
            }
        }
    }

    fun createRemoteTask(todoItem: ToDoItem) =
        viewModelScope.launch(scope) {
            repository.createRemoteTask(todoItem)
        }

    fun clearTask() {
        _currentItem.value = ToDoItem()
    }

    fun deleteRemoteTask(id: String) =
        viewModelScope.launch(scope) {
            repository.deleteRemoteTask(id)
        }

    fun updateRemoteTask(todoItem: ToDoItem) =
        viewModelScope.launch(scope) {
            repository.updateRemoteTask(todoItem.copy(done = !todoItem.done))
        }

    fun deleteAll() =
        viewModelScope.launch(scope) {
            repository.deleteAll()
        }

    fun deleteToken() = repository.deleteToken()


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        scope.cancel()
    }
}