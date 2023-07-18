package com.kostyarazboynik.todoapp.ui.view.fragments.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kostyarazboynik.todoapp.appComponent
import com.kostyarazboynik.todoapp.databinding.FragmentAuthBinding
import com.kostyarazboynik.todoapp.ioc.fragments.auth.AuthFragmentComponent
import com.kostyarazboynik.todoapp.ioc.fragments.auth.AuthFragmentViewComponent
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

    private val viewModel: AuthViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    override fun onAttach(context: Context) {
        context.appComponent.fragmentSubComponent().create().inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent = AuthFragmentComponent(fragment = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthBinding.inflate(layoutInflater)

        fragmentViewComponent = AuthFragmentViewComponent(
            binding = binding,
            navController = findNavController(),
            viewModel = viewModel,
            fragment = fragmentComponent.fragment,
            yandexAuthSdk = yandexAuthSdk,
            context = requireContext()
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
