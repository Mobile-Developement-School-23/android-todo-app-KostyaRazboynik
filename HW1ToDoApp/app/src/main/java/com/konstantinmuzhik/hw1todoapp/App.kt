package com.konstantinmuzhik.hw1todoapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.repository.SharedPreferencesRepository
import com.konstantinmuzhik.hw1todoapp.data.datasource.remote.RetrofitToDoSource
import com.konstantinmuzhik.hw1todoapp.data.datasource.local.ToDoItemDatabase
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepository
import com.konstantinmuzhik.hw1todoapp.di.AppComponent
import com.konstantinmuzhik.hw1todoapp.di.DaggerAppComponent
import com.konstantinmuzhik.hw1todoapp.ioc.ApplicationComponent
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.NetworkConnectivityObserver
import javax.inject.Inject

class App : Application() {
    private lateinit var appComponent: AppComponent

    @Inject
    lateinit var sharedRepository: SharedPreferencesRepository

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(applicationContext)
        appComponent.inject(this)
        setTheme()
    }

    fun getAppComponent() = appComponent


    private fun setTheme(){
        when(sharedRepository.getTheme()){
            0->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            1->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            2->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

//    val applicationComponent by lazy { ApplicationComponent() }
//
//    companion object {
//        /**
//         * Shortcut to get [App] instance from any context, e.g. from Activity.
//         */
//        fun get(context: Context): App = context.applicationContext as App
//    }
}