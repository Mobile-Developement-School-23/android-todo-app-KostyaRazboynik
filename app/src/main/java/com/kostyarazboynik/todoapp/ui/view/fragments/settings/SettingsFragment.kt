package com.kostyarazboynik.todoapp.ui.view.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kostyarazboynik.todoapp.appComponent
import com.kostyarazboynik.todoapp.databinding.FragmentSettingsBinding
import com.kostyarazboynik.todoapp.ioc.fragments.settings.SettingsFragmentComponent
import com.kostyarazboynik.todoapp.ioc.fragments.settings.SettingsFragmentViewComponent

/**
 * Settings Fragment
 *
 * @author Kovalev Konstantin
 *
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var fragmentComponent: SettingsFragmentComponent
    private var fragmentViewComponent: SettingsFragmentViewComponent? = null

    private val viewModel: YandexAuthViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentComponent = SettingsFragmentComponent(fragment = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)

        fragmentViewComponent = SettingsFragmentViewComponent(
            binding = binding,
            navController = findNavController(),
            viewModel = viewModel,
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            fragment = fragmentComponent.fragment
        ).apply {
            settingsViewController.setUpViews()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        fragmentViewComponent = null
    }
}