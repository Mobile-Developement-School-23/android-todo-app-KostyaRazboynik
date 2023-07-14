package com.konstantinmuzhik.hw1todoapp.data

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.konstantinmuzhik.hw1todoapp.App
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepositoryImpl
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Period Work Manager
 *
 * @author Kovalev Konstantin
 *
 */
class PeriodWorkManager(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    @Inject
    lateinit var repository: ToDoItemsRepositoryImpl

    override fun doWork(): Result {
        (context.applicationContext as App).appComponent.inject(this)
        mergeData()
        return Result.success()
    }

    private fun mergeData() = runBlocking {
        return@runBlocking repository.getRemoteToDoItemsFlow()
    }

}