package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.xml


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.konstantinmuzhik.hw1todoapp.appComponent
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAddBinding
import com.konstantinmuzhik.hw1todoapp.ioc.fragments.add.AddFragmentComponent
import com.konstantinmuzhik.hw1todoapp.ioc.fragments.add.AddFragmentViewComponent
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.ui.utils.SharedViewHelper


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

    private val mSharedViewHelper = SharedViewHelper
    private val viewModel: ToDoItemViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)

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
            binding,
            mSharedViewHelper,
            viewModel,
            navController = findNavController(),
            requireContext(),
            viewLifecycleOwner
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