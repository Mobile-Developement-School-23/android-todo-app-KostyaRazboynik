package com.konstantinmuzhik.hw1todoapp.ioc.fragments.add

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAddBinding
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.xml.AddViewController
import com.konstantinmuzhik.hw1todoapp.ui.utils.SharedViewHelper

class AddFragmentViewComponent (
    binding: FragmentAddBinding,
    mSharedViewModel: SharedViewHelper,
    viewModel: ToDoItemViewModel,
    navController: NavController,
    context: Context,
    lifecycleOwner: LifecycleOwner
) {

    val addViewController = AddViewController(
        binding,
        mSharedViewModel,
        viewModel,
        navController,
        context,
        lifecycleOwner
    )
}