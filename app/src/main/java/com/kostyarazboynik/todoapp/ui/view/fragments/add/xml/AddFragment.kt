package com.kostyarazboynik.todoapp.ui.view.fragments.add.xml


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kostyarazboynik.todoapp.appComponent
import com.kostyarazboynik.todoapp.databinding.FragmentAddBinding
import com.kostyarazboynik.todoapp.ioc.fragments.add.AddFragmentComponent
import com.kostyarazboynik.todoapp.ioc.fragments.add.AddFragmentViewComponent
import com.kostyarazboynik.todoapp.ui.utils.SharedViewHelper
import com.kostyarazboynik.todoapp.ui.viewmodels.ToDoItemViewModel


/**
 * Add Fragment
 *
 * @author Kovalev Konstantin
 *
 */
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var fragmentComponent: AddFragmentComponent
    private var fragmentViewComponent: AddFragmentViewComponent? = null

    private val sharedViewHelper = SharedViewHelper
    private val viewModel: ToDoItemViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentComponent = AddFragmentComponent(fragment = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentAddBinding.inflate(layoutInflater)

        viewModel.setToDoItem()

        fragmentViewComponent = AddFragmentViewComponent(
            binding = binding,
            sharedViewHelper = sharedViewHelper,
            viewModel = viewModel,
            navController = findNavController(),
            context = requireContext(),
            lifecycleOwner = viewLifecycleOwner
        ).apply {
            addViewController.setUpViewModel()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
        _binding = null
    }
}