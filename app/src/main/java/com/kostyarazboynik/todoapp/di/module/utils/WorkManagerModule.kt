package com.kostyarazboynik.todoapp.di.module.utils

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import com.kostyarazboynik.todoapp.utils.PeriodWorkManager
import com.kostyarazboynik.todoapp.di.customscope.AppScope
import com.kostyarazboynik.todoapp.utils.Constants.REPEAT_INTERVAL
import com.kostyarazboynik.todoapp.utils.Constants.WORK_MANAGER_TAG
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit


@Module
object WorkManagerModule {

    @AppScope
    @Provides
    fun provideConstraints(): Constraints =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    @AppScope
    @Provides
    fun provideWorkManager(constraints: Constraints): PeriodicWorkRequest =
        PeriodicWorkRequest
            .Builder(PeriodWorkManager::class.java, REPEAT_INTERVAL, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(WORK_MANAGER_TAG)
            .build()
}

