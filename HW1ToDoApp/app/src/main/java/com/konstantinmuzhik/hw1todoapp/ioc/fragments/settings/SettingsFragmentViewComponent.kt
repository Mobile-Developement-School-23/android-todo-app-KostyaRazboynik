package com.konstantinmuzhik.hw1todoapp.ioc.fragments.settings

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentSettingsBinding
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings.SettingsViewController
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.YandexAuthViewModel


class SettingsFragmentViewComponent (
    binding: FragmentSettingsBinding,
    navController: NavController,
    sharedRepository: SharedPreferencesAppSettings,
    viewModel: YandexAuthViewModel,
    lifecycleOwner: LifecycleOwner,
    fragment: Fragment
) {

    val settingsViewController = SettingsViewController(
        binding,
        navController,
        sharedRepository,
        viewModel,
        lifecycleOwner,
        fragment
    )
}