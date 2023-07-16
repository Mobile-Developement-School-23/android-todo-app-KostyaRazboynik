package com.konstantinmuzhik.hw1todoapp.di.module

import com.konstantinmuzhik.hw1todoapp.di.customscope.AppScope
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
