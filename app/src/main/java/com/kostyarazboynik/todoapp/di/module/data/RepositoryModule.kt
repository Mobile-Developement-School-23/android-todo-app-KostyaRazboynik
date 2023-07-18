package com.kostyarazboynik.todoapp.di.module.data

import com.kostyarazboynik.todoapp.data.repository.ToDoItemsRepositoryImpl
import com.kostyarazboynik.todoapp.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface RepositoryModule {

    @Reusable
    @Binds
    fun bindTodoRepository(todoRepository: ToDoItemsRepositoryImpl): Repository
}
