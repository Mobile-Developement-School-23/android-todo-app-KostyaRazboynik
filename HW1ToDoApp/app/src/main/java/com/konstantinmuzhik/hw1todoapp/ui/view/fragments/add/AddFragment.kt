package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.konstantinmuzhik.hw1todoapp.appComponent
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAddBinding
import com.konstantinmuzhik.hw1todoapp.ioc.add.AddFragmentComponent
import com.konstantinmuzhik.hw1todoapp.ioc.add.AddFragmentViewComponent
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.SharedViewHelper
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var fragmentComponent: AddFragmentComponent
    private var fragmentViewComponent: AddFragmentViewComponent? = null

    private val mSharedViewHelper = SharedViewHelper
    private val mToDoViewModel: ToDoItemViewModel by activityViewModels { requireContext().appComponent.findViewModelFactory() }

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

        fragmentViewComponent = AddFragmentViewComponent(
            binding,
            mSharedViewHelper,
            mToDoViewModel,
            navController = findNavController(),
            fragmentComponent.fragment
        ).apply {
            addViewController.setUpViews()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
        _binding = null
    }
}