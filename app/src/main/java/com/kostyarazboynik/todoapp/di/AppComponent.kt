package com.kostyarazboynik.todoapp.di

import android.content.Context
import com.kostyarazboynik.todoapp.App
import com.kostyarazboynik.todoapp.di.customscope.AppScope
import com.kostyarazboynik.todoapp.di.module.ApplicationModule
import com.kostyarazboynik.todoapp.di.module.data.DataBaseModule
import com.kostyarazboynik.todoapp.di.module.data.DataSourceModule
import com.kostyarazboynik.todoapp.di.module.data.RepositoryModule
import com.kostyarazboynik.todoapp.di.module.network.NetworkModule
import com.kostyarazboynik.todoapp.di.module.network.NetworkObserver
import com.kostyarazboynik.todoapp.di.module.network.ToDoNetworkModule
import com.kostyarazboynik.todoapp.di.module.network.YandexPassportNetworkModule
import com.kostyarazboynik.todoapp.di.module.utils.WorkManagerModule
import com.kostyarazboynik.todoapp.di.subcomponents.AuthSubComponent
import com.kostyarazboynik.todoapp.domain.notifications.NotificationPostponeReceiver
import com.kostyarazboynik.todoapp.domain.notifications.NotificationsReceiver
import com.kostyarazboynik.todoapp.ui.view.activity.MainActivity
import com.kostyarazboynik.todoapp.ui.viewmodels.ViewModelFactory
import com.kostyarazboynik.todoapp.utils.PeriodWorkManager
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
        NetworkModule::class,
        ToDoNetworkModule::class,
        YandexPassportNetworkModule::class,
        DataBaseModule::class,
        DataSourceModule::class,
        RepositoryModule::class,
        WorkManagerModule::class,
        ApplicationModule::class,
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

    fun inject(workManager: PeriodWorkManager)

    fun inject(app: App)
    fun inject(activity: MainActivity)

    fun fragmentSubComponent(): AuthSubComponent.Factory

}
