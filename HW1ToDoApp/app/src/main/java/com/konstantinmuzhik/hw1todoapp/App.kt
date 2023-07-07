package com.konstantinmuzhik.hw1todoapp

import android.app.Application
import android.content.Context
import com.konstantinmuzhik.hw1todoapp.di.AppComponent
import com.konstantinmuzhik.hw1todoapp.di.DaggerAppComponent
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .context(context = applicationContext)
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }
