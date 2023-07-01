package com.konstantinmuzhik.hw1todoapp

import android.app.Application
import android.content.Context
import com.konstantinmuzhik.hw1todoapp.data.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.data.retrofit.RetrofitToDoSource
import com.konstantinmuzhik.hw1todoapp.data.room.ToDoItemDatabase
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepository
import com.konstantinmuzhik.hw1todoapp.utils.ServiceLocator
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.NetworkConnectivityObserver
import com.konstantinmuzhik.hw1todoapp.utils.locale

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        ServiceLocator.register<Context>(this)

        ServiceLocator.register(ToDoItemDatabase.getDatabase(locale()))
        ServiceLocator.register(RetrofitToDoSource().makeRetrofitService())
        ServiceLocator.register(SharedPreferencesAppSettings(locale()))
        ServiceLocator.register(NetworkConnectivityObserver(this))
        ServiceLocator.register(ToDoItemsRepository(locale(), locale(), locale()))
    }
}