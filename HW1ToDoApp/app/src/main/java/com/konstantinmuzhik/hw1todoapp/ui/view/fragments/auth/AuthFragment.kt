package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.konstantinmuzhik.hw1todoapp.appComponent
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAuthBinding
import com.konstantinmuzhik.hw1todoapp.ioc.fragments.auth.AuthFragmentComponent
import com.konstantinmuzhik.hw1todoapp.ioc.fragments.auth.AuthFragmentViewComponent
import com.yandex.authsdk.YandexAuthSdk
import javax.inject.Inject

/**
 * Auth Fragment
 *
 * @author Kovalev Konstantin
 *
 */
class AuthFragment : Fragment() {

    private lateinit var fragmentComponent: AuthFragmentComponent
    private var fragmentViewComponent: AuthFragmentViewComponent? = null

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var yandexAuthSdk: YandexAuthSdk

    @Inject
    lateinit var sharedPreferences: SharedPreferencesAppSettings

    private val viewModel: AuthViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)

        fragmentComponent = AuthFragmentComponent(fragment = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthBinding.inflate(layoutInflater)

        fragmentViewComponent = AuthFragmentViewComponent(
            binding,
            navController = findNavController(),
            sharedPreferences,
            viewModel,
            fragmentComponent.fragment,
            yandexAuthSdk
        ).apply {
            authViewController.setUpViews()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
        _binding = null
    }
}
