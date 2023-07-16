package com.konstantinmuzhik.hw1todoapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.di.AppComponent
import com.konstantinmuzhik.hw1todoapp.di.DaggerAppComponent
import com.konstantinmuzhik.hw1todoapp.utils.Constants.WORK_MANAGER_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject


/**
 * App
 *
 * @author Kovalev Konstantin
 *
 */
class App : Application() {

    @Inject
    lateinit var myWorkRequest: PeriodicWorkRequest

    @Inject
    lateinit var sharedRepository: SharedPreferencesAppSettings

    @Inject
    lateinit var coroutineScope: CoroutineScope

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory()
            .create(context = applicationContext)

        appComponent.inject(this)

        periodicUpdate()
        setTheme()
        setNotificationChannel()
    }

    private fun setNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "channel_name",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

        }
    }

    override fun onTerminate() {
        super.onTerminate()
        coroutineScope.cancel()
    }

    private fun periodicUpdate() =
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORK_MANAGER_TAG,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            myWorkRequest
        )

    private fun setTheme() =
        when (sharedRepository.getTheme()) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }
