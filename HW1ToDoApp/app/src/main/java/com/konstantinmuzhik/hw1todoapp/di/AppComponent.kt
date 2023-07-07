package com.konstantinmuzhik.hw1todoapp.di

import android.content.Context
import com.konstantinmuzhik.hw1todoapp.ui.view.ViewModelFactory
import com.konstantinmuzhik.hw1todoapp.di.module.ApiModule
import com.konstantinmuzhik.hw1todoapp.di.module.DataBaseModule
import com.konstantinmuzhik.hw1todoapp.di.module.DataSourceModule
import com.konstantinmuzhik.hw1todoapp.di.module.NetworkObserver
import com.konstantinmuzhik.hw1todoapp.di.module.RepositoryModule
import com.konstantinmuzhik.hw1todoapp.di.module.YandexPassportApiModule
import com.konstantinmuzhik.hw1todoapp.ui.view.activity.MainActivity
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.AddFragment
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth.AuthFragment
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.main.MainFragment
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings.SettingsFragment
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.update.UpdateFragment
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.YandexAuthViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * AppComponent
 *
 * @author Kovalev Konstantin
 *
 */
@Singleton
@Component(
    modules = [
        RepositoryModule::class,
        DataBaseModule::class,
        ApiModule::class,
        NetworkObserver::class,
        DataSourceModule::class,
        YandexPassportApiModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

    fun toDoItemViewModel(): ToDoItemViewModel
    fun yandexAuthViewModel(): YandexAuthViewModel
    fun findViewModelFactory(): ViewModelFactory

    fun inject(mainFragment: MainFragment)
    fun inject(addFragment: AddFragment)
    fun inject(updateFragment: UpdateFragment)
    fun inject(fragment: AuthFragment)
    fun inject(fragment: SettingsFragment)

    fun inject(activity: MainActivity)
}
