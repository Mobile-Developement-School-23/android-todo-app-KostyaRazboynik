package com.kostyarazboynik.todoapp.di.module

import com.kostyarazboynik.todoapp.di.customscope.AppScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
object ApplicationModule {

    @AppScope
    @Provides
    fun provideScope() = CoroutineScope(SupervisorJob())

}
