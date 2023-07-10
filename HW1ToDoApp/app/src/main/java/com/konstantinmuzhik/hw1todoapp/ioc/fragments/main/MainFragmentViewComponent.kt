package com.konstantinmuzhik.hw1todoapp.ioc.fragments.main

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentMainBinding
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.main.MainViewController
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel

class MainFragmentViewComponent (
    binding: FragmentMainBinding,
    mToDoItemViewModel: ToDoItemViewModel,
    navController: NavController,
    context: Context,
    lifecycleOwner: LifecycleOwner
) {

    val mainViewController = MainViewController(
        binding,
        mToDoItemViewModel,
        navController,
        context,
        lifecycleOwner
    )
}