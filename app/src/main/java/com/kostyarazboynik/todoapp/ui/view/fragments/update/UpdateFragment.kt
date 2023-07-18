package com.kostyarazboynik.todoapp.ui.view.fragments.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kostyarazboynik.todoapp.appComponent
import com.kostyarazboynik.todoapp.databinding.FragmentUpdateBinding
import com.kostyarazboynik.todoapp.ioc.fragments.update.UpdateFragmentComponent
import com.kostyarazboynik.todoapp.ioc.fragments.update.UpdateFragmentViewComponent
import com.kostyarazboynik.todoapp.ui.utils.SharedViewHelper
import com.kostyarazboynik.todoapp.ui.viewmodels.ToDoItemViewModel


/**
 * Update Fragment
 *
 * @author Kovalev Konstantin
 *
 */
class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var fragmentComponent: UpdateFragmentComponent
    private var fragmentViewComponent: UpdateFragmentViewComponent? = null

    private val sharedViewHelper = SharedViewHelper
    private val viewModel: ToDoItemViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    private val args: UpdateFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentComponent = UpdateFragmentComponent(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentUpdateBinding.inflate(layoutInflater)
        binding.args = args

        viewModel.getToDoItem(args.currentItem.id)

        fragmentViewComponent = UpdateFragmentViewComponent(
            binding = binding,
            sharedViewHelper = sharedViewHelper,
            viewModel = viewModel,
            navController = findNavController(),
            context = requireContext(),
            lifecycleOwner = viewLifecycleOwner
        ).apply {
            updateViewController.setUpViewModel()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
        _binding = null
    }
}