package com.konstantinmuzhik.hw1todoapp.data

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Period Work Manager
 *
 * @author Kovalev Konstantin
 *
 */
class PeriodWorkManager(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result =
        Result.success()

}