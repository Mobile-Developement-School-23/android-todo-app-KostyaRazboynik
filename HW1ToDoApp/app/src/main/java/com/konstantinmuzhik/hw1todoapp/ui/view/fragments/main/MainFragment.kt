package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.konstantinmuzhik.hw1todoapp.appComponent
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentMainBinding
import com.konstantinmuzhik.hw1todoapp.ioc.fragments.main.MainFragmentComponent
import com.konstantinmuzhik.hw1todoapp.ioc.fragments.main.MainFragmentViewComponent

/**
 * Main Fragment
 *
 * @author Kovalev Konstantin
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var fragmentComponent: MainFragmentComponent
    private var fragmentViewComponent: MainFragmentViewComponent? = null

    private val viewModel: MainViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent = MainFragmentComponent(fragment = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(layoutInflater)


        fragmentViewComponent = MainFragmentViewComponent(
            binding,
            viewModel,
            navController = findNavController(),
            context = requireContext(),
            viewLifecycleOwner,
            layoutInflater
        ).apply {
            mainViewController.setUpViews()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
        _binding = null
    }
}