package com.konstantinmuzhik.hw1todoapp.di

import android.content.Context
import com.konstantinmuzhik.hw1todoapp.App
import com.konstantinmuzhik.hw1todoapp.di.scope.AppScope
import com.konstantinmuzhik.hw1todoapp.ui.MainActivity
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.AddFragment
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth.AuthFragment
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth.AuthViewController
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth.AuthYandexController
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings.SettingsFragment
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.utils.PeriodWorkManager
import dagger.BindsInstance
import dagger.Component

@Component(modules = [NetworkModule::class])
@AppScope
interface AppComponent {
    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }

    fun toDoItemViewModel(): ToDoItemViewModel

    fun inject(fragment: SettingsFragment)
    fun inject(fragment: AuthFragment)
    fun inject(addFragment: AddFragment)


    fun inject(worker: PeriodWorkManager)
    fun inject(app: App)
    fun inject(activity: MainActivity)

}
