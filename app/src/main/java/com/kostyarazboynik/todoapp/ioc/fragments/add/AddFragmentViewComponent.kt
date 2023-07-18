package com.kostyarazboynik.todoapp.ioc.fragments.add

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.kostyarazboynik.todoapp.databinding.FragmentAddBinding
import com.kostyarazboynik.todoapp.ui.viewmodels.ToDoItemViewModel
import com.kostyarazboynik.todoapp.ui.view.fragments.add.xml.AddViewController
import com.kostyarazboynik.todoapp.ui.utils.SharedViewHelper

class AddFragmentViewComponent (
    binding: FragmentAddBinding,
    sharedViewHelper: SharedViewHelper,
    viewModel: ToDoItemViewModel,
    navController: NavController,
    context: Context,
    lifecycleOwner: LifecycleOwner
) {

    val addViewController = AddViewController(
        binding = binding,
        sharedViewHelper = sharedViewHelper,
        viewModel = viewModel,
        navController = navController,
        context = context,
        lifecycleOwner = lifecycleOwner
    )
}