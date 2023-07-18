package com.kostyarazboynik.todoapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.kostyarazboynik.todoapp.data.datasource.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.di.AppComponent
import com.kostyarazboynik.todoapp.di.DaggerAppComponent
import com.kostyarazboynik.todoapp.utils.Constants.THEME_NIGHT_NO
import com.kostyarazboynik.todoapp.utils.Constants.THEME_NIGHT_YES
import com.kostyarazboynik.todoapp.utils.Constants.WORK_MANAGER_TAG
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
    lateinit var sharedPreferences: SharedPreferencesAppSettings

    @Inject
    lateinit var coroutineScope: CoroutineScope

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)

        periodicUpdate()
        setTheme()
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
        when (sharedPreferences.getTheme()) {
            THEME_NIGHT_NO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_NIGHT_YES -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }
