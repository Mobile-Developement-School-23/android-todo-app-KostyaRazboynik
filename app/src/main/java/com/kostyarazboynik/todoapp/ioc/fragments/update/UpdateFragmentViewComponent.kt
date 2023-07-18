package com.kostyarazboynik.todoapp.ioc.fragments.update

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.kostyarazboynik.todoapp.databinding.FragmentUpdateBinding
import com.kostyarazboynik.todoapp.ui.utils.SharedViewHelper
import com.kostyarazboynik.todoapp.ui.view.fragments.update.UpdateViewController
import com.kostyarazboynik.todoapp.ui.viewmodels.ToDoItemViewModel

class UpdateFragmentViewComponent (
    binding: FragmentUpdateBinding,
    sharedViewHelper: SharedViewHelper,
    viewModel: ToDoItemViewModel,
    navController: NavController,
    context: Context,
    lifecycleOwner: LifecycleOwner
) {

    val updateViewController = UpdateViewController(
        binding = binding,
        sharedViewHelper = sharedViewHelper,
        viewModel = viewModel,
        navController = navController,
        context = context,
        lifecycleOwner = lifecycleOwner
    )
}