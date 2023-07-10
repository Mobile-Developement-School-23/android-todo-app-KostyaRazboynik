package com.konstantinmuzhik.hw1todoapp.ioc.fragments.add

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAddBinding
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.AddViewController
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.SharedViewHelper
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel

class AddFragmentViewComponent (
    binding: FragmentAddBinding,
    mSharedViewModel: SharedViewHelper,
    mToDoViewModel: ToDoItemViewModel,
    navController: NavController,
    fragment: Fragment
) {

    val addViewController = AddViewController(
        binding,
        mSharedViewModel,
        mToDoViewModel,
        navController,
        fragment
    )
}