package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.konstantinmuzhik.hw1todoapp.App
import com.konstantinmuzhik.hw1todoapp.appComponent
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentSettingsBinding
import com.konstantinmuzhik.hw1todoapp.ioc.fragments.settings.SettingsFragmentComponent
import com.konstantinmuzhik.hw1todoapp.ioc.fragments.settings.SettingsFragmentViewComponent
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.YandexAuthViewModel
import javax.inject.Inject

/**
 * Settings Fragment
 *
 * @author Kovalev Konstantin
 *
 */
class SettingsFragment : Fragment() {

    private lateinit var fragmentComponent: SettingsFragmentComponent
    private var fragmentViewComponent: SettingsFragmentViewComponent? = null

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedRepository: SharedPreferencesAppSettings

    private val viewModel: YandexAuthViewModel by viewModels {
        YandexAuthViewModelFactory {
            (requireActivity().application as App).appComponent.yandexAuthViewModel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)

        fragmentComponent = SettingsFragmentComponent(fragment = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)

        fragmentViewComponent = SettingsFragmentViewComponent(
            binding,
            navController = findNavController(),
            sharedRepository,
            viewModel,
            viewLifecycleOwner,
            fragmentComponent.fragment

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