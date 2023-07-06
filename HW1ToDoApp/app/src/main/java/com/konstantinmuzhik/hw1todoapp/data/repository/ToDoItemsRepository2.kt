package com.konstantinmuzhik.hw1todoapp.data.repository

class ToDoItemsRepository2{
//    @Inject constructor(
//    database: ToDoItemDatabase,
//    private val todoService: ToDoItemApi,
//    private val sharedPrefRepository: SharedPreferencesRepository,
//) {
//    companion object{
//        private const val DEFAULT_TOKEN = "Bearer astely"
//    }
//    private var token: String = DEFAULT_TOKEN
//    private val todoDao: ToDoItemDao by lazy {
//        database.toDoItemDao()
//    }
//    val todoItems: Flow<List<ToDoItem>> =
//        todoDao.getToDoItemsFlow().map { list -> list.map { it.toToDoItem() } }
//
//    init {
//        val savedToken = sharedPrefRepository.getAuthToken()
//        if (!savedToken.isNullOrEmpty()) {
//            token = savedToken
//            println(token)
//        }
//    }
//
//    fun login(newToken: String) {
//        token = newToken
//    }
//    fun logout(){
//        token = DEFAULT_TOKEN
//    }
//
//    suspend fun addItem(todoItem: ToDoItem) {
//        val newTodoItem = todoItem
//        todoDao.createItem(ToDoItemEntity.fromToDoItem(newTodoItem))
//        val newItem = TodoNetworkMapper.modelToEntity(newTodoItem)
//        val requestItem = ToDoItemRequest(element = newItem)
//        try {
//            val response =
//                todoService.addToDoItem(token, sharedPrefRepository.getRevision(), requestItem)
//            if (response.isSuccessful) {
//                val result = response.body()
//                if (result != null) {
//                    sharedPrefRepository.writeRevision(result.revision)
//                }
//            } else {
//                updateItems()
//            }
//        } catch (_: Exception) {
//
//        }
//    }
//
//    suspend fun getItemById(id: String): ToDoItem {
//        val todoItemEntity = todoDao.getToDoItemById(id)
//        return todoItemEntity.toToDoItem()
//    }
//
//    suspend fun updateItem(todoItem: ToDoItem, withUpdate: Boolean) {
//        var newTodoItem = todoItem
//        if (withUpdate) {
//            newTodoItem = todoItem
//        }
//        todoDao.updateToDoItem(ToDoItemEntity.fromToDoItem(newTodoItem))
//        val newItem = TodoNetworkMapper.modelToEntity(newTodoItem)
//        val requestItem = ToDoItemRequest(element = newItem)
//        try {
//            val response = todoService.updateToDoItem(
//                token,
//                sharedPrefRepository.getRevision(),
//                newItem.id,
//                requestItem
//            )
//            if (response.isSuccessful) {
//                val result = response.body()
//                if (result != null) {
//                    sharedPrefRepository.writeRevision(result.revision)
//                }
//            } else {
//                updateItems()
//            }
//        } catch (_: Exception) {
//        }
//    }
//
//    suspend fun getItemsByPriority(priority: Priority) =
//        todoDao.loadTodoItemsByImportance(ImportanceMapper.priorityToImportance(priority))
//            .map { it.toToDoItem() }
//
//    suspend fun deleteItem(id: String) {
//        todoDao.deleteById(id)
//        try {
//            val response = todoService.deleteTodoItem(token, sharedPrefRepository.getRevision(), id)
//            if (response.isSuccessful) {
//                val result = response.body()
//                if (result != null) {
//                    sharedPrefRepository.writeRevision(result.revision)
//                }
//            } else {
//                updateItems()
//            }
//        } catch (_: Exception) {
//
//        }
//    }
//
//    suspend fun loadAllItems(func: (value: Boolean) -> Unit = {}) {
//        try {
//            val response = todoService.getList(token)
//            if (response.isSuccessful) {
//                val result = response.body()
//                if (result != null) {
//                    todoDao.rewriteTable(result.list.map {
//                        ToDoItemEntity.fromToDoItem(
//                            TodoNetworkMapper.entityToModel(it)
//                        )
//                    })
//                    sharedPrefRepository.writeRevision(result.revision)
//                    func(true)
//                } else {
//                    func(false)
//                }
//            } else {
//                func(false)
//            }
//        } catch (e: Exception) {
//            func(false)
//        }
//    }
//
//    suspend fun getAllItems(): List<ToDoItem> =
//        todoDao.getToDoItems().map { it.toToDoItem() }
//
//    suspend fun updateItems(func: (value: Boolean) -> Unit = {}) {
//        try {
//            val response = todoService.getList(token)
//            if (response.isSuccessful) {
//                val body = response.body()
//                if (body != null) {
//                    val savedItems = getAllItems().toMutableList()
//                    val loadedItems = body.list.toMutableList()
//                    for (i in savedItems) { //Тут должна быть рабочая синхронизация, но удаление чет пошло по ж***
//                        if (loadedItems.all { it.id != i.id }) {
//                            loadedItems.add(TodoNetworkMapper.modelToEntity(i))
//                        } else {
//                            val index = loadedItems.indexOfFirst { it.id == i.id }
//                            if (index != -1) {
//                                loadedItems[index] = TodoNetworkMapper.modelToEntity(i)
//                            }
//                        }
//                    }
//                    todoDao.rewriteTable(body.list.map {
//                       ToDoItemEntity.fromToDoItem(
//                            TodoNetworkMapper.entityToModel(
//                                it
//                            )
//                        )
//                    })
//                    func(true)
//                    val updateResponse = todoService.updateList(
//                        token,
//                        sharedPrefRepository.getRevision(),
//                        ToDoItemListRequest(status = "ok", loadedItems)
//                    )
//                    if (updateResponse.isSuccessful) {
//                        val updateBody = updateResponse.body()
//                        if (updateBody != null) {
//                            todoDao.rewriteTable(
//                                updateBody.list.map {
//                                    ToDoItemEntity.fromToDoItem(
//                                        TodoNetworkMapper.entityToModel(
//                                            it
//                                        )
//                                    )
//                                }
//                            )
//                            sharedPrefRepository.writeRevision(updateBody.revision)
//                        }
//                    }
//                }
//            }
//            else{
//                func(false)
//            }
//        } catch (e: Exception) {
//            Log.e("Repository", e.toString())
//            func(false)
//        }
//    }
}