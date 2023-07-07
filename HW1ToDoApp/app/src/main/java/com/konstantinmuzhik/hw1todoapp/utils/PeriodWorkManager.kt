package com.konstantinmuzhik.hw1todoapp.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class PeriodWorkManager(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {


    override fun doWork(): Result =
        Result.success()

}