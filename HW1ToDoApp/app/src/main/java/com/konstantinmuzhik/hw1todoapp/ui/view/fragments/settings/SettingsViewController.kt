package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings

import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.repository.SharedPreferencesRepository

class SettingsViewController(
    rootView: View,
    private val navController: NavController,
    private val sharedRepository: SharedPreferencesRepository
) {

    private val closeButton: ImageView = rootView.findViewById(R.id.close)
    private val themeSelector: RadioGroup = rootView.findViewById(R.id.theme_selector)

    fun setUpViews() {
        setUpSelector()
        setUpSelectorListener()
        setUpCloseButton()
    }

    private fun setUpCloseButton() =
        closeButton.setOnClickListener {
            navController.popBackStack()
        }

    private fun setUpSelectorListener() =
        themeSelector.setOnCheckedChangeListener { _, checkedId ->
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
            0 -> themeSelector.check(R.id.light_theme_button)
            1 -> themeSelector.check(R.id.dark_theme_button)
            2 -> themeSelector.check(R.id.system_theme_button)
        }

        themeSelector.jumpDrawablesToCurrentState()
    }

}