package com.konstantinmuzhik.hw1todoapp.di.module

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import com.konstantinmuzhik.hw1todoapp.data.PeriodWorkManager
import com.konstantinmuzhik.hw1todoapp.di.customscope.AppScope
import com.konstantinmuzhik.hw1todoapp.utils.Constants.REPEAT_INTERVAL
import com.konstantinmuzhik.hw1todoapp.utils.Constants.WORK_MANAGER_TAG
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit


@Module
object WorkManagerModule {

    @AppScope
    @Provides
    fun provideConstraints(): Constraints = Constraints.Builder()
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

