package com.kostyarazboynik.todoapp.ioc.fragments.settings

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.kostyarazboynik.todoapp.databinding.FragmentSettingsBinding
import com.kostyarazboynik.todoapp.ui.view.fragments.settings.SettingsViewController
import com.kostyarazboynik.todoapp.ui.view.fragments.settings.YandexAuthViewModel


class SettingsFragmentViewComponent (
    binding: FragmentSettingsBinding,
    navController: NavController,
    viewModel: YandexAuthViewModel,
    lifecycleOwner: LifecycleOwner,
    context: Context,
    fragment: Fragment
) {

    val settingsViewController = SettingsViewController(
        binding = binding,
        navController = navController,
        viewModel = viewModel,
        lifecycleOwner = lifecycleOwner,
        context = context,
        fragment = fragment
    )
}