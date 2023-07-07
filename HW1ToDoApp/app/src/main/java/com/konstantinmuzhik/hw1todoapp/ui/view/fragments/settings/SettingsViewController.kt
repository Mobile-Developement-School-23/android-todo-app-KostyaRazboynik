package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentSettingsBinding
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.YandexAuthViewModel
import kotlinx.coroutines.launch

/**
 * Settings Fragment View Controller
 *
 * @author Kovalev Konstantin
 *
 */
class SettingsViewController(
    private val binding: FragmentSettingsBinding,
    private val navController: NavController,
    private val sharedRepository: SharedPreferencesAppSettings,
    private val viewModel: YandexAuthViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val fragment: Fragment
) {

    fun setUpViews() {
        setUpSelector()
        setUpSelectorListener()
        setUpCloseButton()
        collectAccountInfo()
    }

    private fun collectAccountInfo() {
        lifecycleOwner.lifecycleScope.launch {
            viewModel.accountInfo.collect {
                if (it != null) binding.loggedName.text = it
                else binding.loggedName.text = fragment.getString(R.string.guest_mode)
            }
        }
    }

    private fun setUpCloseButton() =
        binding.toolbar.setOnClickListener {
            navController.popBackStack()
        }

    private fun setUpSelectorListener() =
        binding.themeSelector.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.light_theme_button -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedRepository.setTheme(0)
                }

                R.id.dark_theme_button -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedRepository.setTheme(1)
                }

                R.id.system_theme_button -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    sharedRepository.setTheme(2)
                }
            }
        }

    private fun setUpSelector() {
        when (sharedRepository.getTheme()) {
            0 -> binding.themeSelector.check(R.id.light_theme_button)
            1 -> binding.themeSelector.check(R.id.dark_theme_button)
            2 -> binding.themeSelector.check(R.id.system_theme_button)
        }
        binding.themeSelector.jumpDrawablesToCurrentState()
    }

}