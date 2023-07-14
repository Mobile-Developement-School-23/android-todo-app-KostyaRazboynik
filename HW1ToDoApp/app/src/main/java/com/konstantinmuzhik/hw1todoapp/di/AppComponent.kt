package com.konstantinmuzhik.hw1todoapp.di

import android.content.Context
import com.konstantinmuzhik.hw1todoapp.App
import com.konstantinmuzhik.hw1todoapp.data.PeriodWorkManager
import com.konstantinmuzhik.hw1todoapp.di.customscope.AppScope
import com.konstantinmuzhik.hw1todoapp.di.module.ApplicationModule
import com.konstantinmuzhik.hw1todoapp.di.module.AuthModule
import com.konstantinmuzhik.hw1todoapp.di.module.DataBaseModule
import com.konstantinmuzhik.hw1todoapp.di.module.DataSourceModule
import com.konstantinmuzhik.hw1todoapp.di.module.NetworkModule
import com.konstantinmuzhik.hw1todoapp.di.module.NetworkObserver
import com.konstantinmuzhik.hw1todoapp.di.module.RepositoryModule
import com.konstantinmuzhik.hw1todoapp.di.module.WorkManagerModule
import com.konstantinmuzhik.hw1todoapp.ui.notifications.NotificationPostponeReceiver
import com.konstantinmuzhik.hw1todoapp.ui.notifications.NotificationsReceiver
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ViewModelFactory
import com.konstantinmuzhik.hw1todoapp.ui.view.activity.MainActivity
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.xml.AddFragment
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth.AuthFragment
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings.SettingsFragment
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.update.UpdateFragment
import dagger.BindsInstance
import dagger.Component

/**
 * AppComponent
 *
 * @author Kovalev Konstantin
 *
 */
@AppScope
@Component(
    modules = [
        NetworkObserver::class,
        ApplicationModule::class,
        NetworkModule::class,
        DataBaseModule::class,
        DataSourceModule::class,
        RepositoryModule::class,
        WorkManagerModule::class,
        AuthModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            context: Context,
        ): AppComponent
    }

    fun findViewModelFactory(): ViewModelFactory

    fun inject(notificationPostponeReceiver: NotificationPostponeReceiver)
    fun inject(notificationsReceiver: NotificationsReceiver)

    fun inject(addFragment: AddFragment)
    fun inject(updateFragment: UpdateFragment)
    fun inject(fragment: AuthFragment)
    fun inject(fragment: SettingsFragment)

    fun inject(workManager: PeriodWorkManager)

    fun inject(app: App)
    fun inject(activity: MainActivity)
}
