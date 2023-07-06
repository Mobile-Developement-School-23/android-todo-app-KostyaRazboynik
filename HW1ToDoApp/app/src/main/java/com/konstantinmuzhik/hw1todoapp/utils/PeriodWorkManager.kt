package com.konstantinmuzhik.hw1todoapp.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.konstantinmuzhik.hw1todoapp.App
import com.konstantinmuzhik.hw1todoapp.data.models.LoadingState
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class PeriodWorkManager(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    @Inject
    lateinit var repository: ToDoItemsRepository

    init {
        (applicationContext as App).getAppComponent().inject(this)
    }

    override fun doWork(): Result =
        when (mergeData()) {
            is LoadingState.Success<*> -> Result.success()
            else -> Result.failure()
        }

    private fun mergeData() = runBlocking {
        return@runBlocking repository.getRemoteToDoItems()
    }
}