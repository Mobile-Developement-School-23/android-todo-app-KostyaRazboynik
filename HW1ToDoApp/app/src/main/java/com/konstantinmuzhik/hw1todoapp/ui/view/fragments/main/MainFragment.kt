package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.konstantinmuzhik.hw1todoapp.appComponent
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentMainBinding
import com.konstantinmuzhik.hw1todoapp.ioc.fragments.main.MainFragmentComponent
import com.konstantinmuzhik.hw1todoapp.ioc.fragments.main.MainFragmentViewComponent
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel

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

    private val mToDoViewModel: ToDoItemViewModel by activityViewModels { requireContext().appComponent.findViewModelFactory() }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mode", mToDoViewModel.modeAll)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireContext().appComponent.inject(this)
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
            mToDoViewModel,
            navController = findNavController(),
            context = requireContext(),
            viewLifecycleOwner
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