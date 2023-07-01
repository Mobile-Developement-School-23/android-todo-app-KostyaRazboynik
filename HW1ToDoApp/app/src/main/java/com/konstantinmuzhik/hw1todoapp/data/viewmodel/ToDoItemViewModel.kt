package com.konstantinmuzhik.hw1todoapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konstantinmuzhik.hw1todoapp.data.models.LoadingState
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepository
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.NetworkConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ToDoItemViewModel(
    private val repository: ToDoItemsRepository,
    private val connection: NetworkConnectivityObserver
) : ViewModel() {

    private var job: Job? = null

    var modeAll: Boolean = false

    private val _status = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    val status: MutableStateFlow<ConnectivityObserver.Status>  = _status

    private val _tasks = MutableStateFlow<List<ToDoItem>>(emptyList())
    val tasks: Flow<List<ToDoItem>> get() = _tasks

    val countCompletedTask = _tasks.map { it.count { it2 -> it2.done } }

    private val _loadingState =
        MutableStateFlow<LoadingState<Any>>(LoadingState.Success("Loaded from rood complete!"))
    val loadingState: StateFlow<LoadingState<Any>> = _loadingState

    private var _currentItem = MutableStateFlow(ToDoItem())
    var currentItem = _currentItem.asStateFlow()

    init {
        observeNetwork()
        loadLocalData()
    }

    fun changeMode() {
        modeAll = !modeAll
        job?.cancel()
        loadLocalData()
    }

    private fun observeNetwork() =
        viewModelScope.launch {
            connection.observe().collectLatest {
                _status.emit(it)
            }
        }

    fun loadLocalData() {
        job = viewModelScope.launch(Dispatchers.IO) {
            _tasks.emitAll(repository.getAllToDoItems())
        }
    }

    fun createToDoItem(todoItem: ToDoItem) =
        viewModelScope.launch {
            repository.createToDoItem(todoItem)
        }

    fun deleteToDoItem(todoItem: ToDoItem) =
        viewModelScope.launch {
            repository.deleteToDoItem(todoItem)
        }

    fun updateToDoItem(todoItem: ToDoItem) =
        viewModelScope.launch {
            repository.updateToDoItem(todoItem)
        }

    fun changeToDoItemDone(todoItem: ToDoItem) =
        viewModelScope.launch {
            repository.updateToDoItemDone(todoItem.id, !todoItem.done)
        }

    fun loadToDoItem(id: String) =
        viewModelScope.launch(Dispatchers.IO) {
            _currentItem.emit(repository.getToDoItemById(id))
        }

    fun loadRemoteList() {
        if (status.value == ConnectivityObserver.Status.Available) {
            _loadingState.value = LoadingState.Loading(true)
            viewModelScope.launch(Dispatchers.IO) {
                _loadingState.emit(repository.getRemoteToDoItems())
            }
        }
    }

    fun createRemoteToDoItem(todoItem: ToDoItem) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.createRemoteToDoItem(todoItem)
        }

    fun clearToDoItem() {
        _currentItem.value = ToDoItem()
    }

    fun deleteRemoteToDoItem(id: String) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRemoteToDoItem(id)
        }

    fun updateRemoteToDoItem(todoItem: ToDoItem) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRemoteToDoItem(todoItem.copy(done = !todoItem.done))
        }

    fun deleteAllToDoItems() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllToDoItems()
        }

    fun deleteToken() = repository.deleteToken()

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}