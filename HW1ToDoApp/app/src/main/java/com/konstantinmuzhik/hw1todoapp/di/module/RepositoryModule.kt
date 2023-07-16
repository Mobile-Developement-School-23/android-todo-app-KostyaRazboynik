package com.konstantinmuzhik.hw1todoapp.di.module

import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepositoryImpl
import com.konstantinmuzhik.hw1todoapp.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface RepositoryModule {

    @Reusable
    @Binds
    fun bindTodoRepository(todoRepository: ToDoItemsRepositoryImpl): Repository
}
