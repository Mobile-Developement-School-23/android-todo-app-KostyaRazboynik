package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.konstantinmuzhik.hw1todoapp.App
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.repository.SharedPreferencesRepository
import com.konstantinmuzhik.hw1todoapp.ioc.settings.SettingsFragmentComponent
import com.konstantinmuzhik.hw1todoapp.ioc.settings.SettingsFragmentViewComponent
import javax.inject.Inject

class SettingsFragment : Fragment() {


    private lateinit var fragmentComponent: SettingsFragmentComponent
    private var fragmentViewComponent: SettingsFragmentViewComponent? = null

    @Inject
    lateinit var sharedRepository: SharedPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as App).getAppComponent().inject(this)

        fragmentComponent = SettingsFragmentComponent(fragment = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        fragmentViewComponent = SettingsFragmentViewComponent(
            root = view,
            navController = findNavController(),
            sharedRepository
        ).apply {
            settingsViewController.setUpViews()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
    }
}