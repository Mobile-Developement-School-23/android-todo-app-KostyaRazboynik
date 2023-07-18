package com.kostyarazboynik.todoapp.di.subcomponents

import com.kostyarazboynik.todoapp.di.customscope.AuthScope
import com.kostyarazboynik.todoapp.di.module.data.AuthModule
import com.kostyarazboynik.todoapp.ui.view.fragments.auth.AuthFragment
import dagger.Subcomponent

@AuthScope
@Subcomponent(
    modules = [
        AuthModule::class
    ]
)
interface AuthSubComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthSubComponent
    }

    fun inject(fragment: AuthFragment)
}