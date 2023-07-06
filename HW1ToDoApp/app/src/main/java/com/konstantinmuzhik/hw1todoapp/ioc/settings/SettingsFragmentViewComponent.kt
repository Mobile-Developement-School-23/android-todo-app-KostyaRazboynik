package com.konstantinmuzhik.hw1todoapp.ioc.settings

import android.view.View
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.data.repository.SharedPreferencesRepository
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings.SettingsViewController


class SettingsFragmentViewComponent (
    root: View,
    navController: NavController,
    sharedRepository: SharedPreferencesRepository
) {

    val settingsViewController = SettingsViewController(
        root,
        navController,
        sharedRepository
    )
}