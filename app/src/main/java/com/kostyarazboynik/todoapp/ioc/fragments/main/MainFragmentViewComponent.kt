package com.kostyarazboynik.todoapp.ioc.fragments.main

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.kostyarazboynik.todoapp.databinding.FragmentMainBinding
import com.kostyarazboynik.todoapp.ui.view.fragments.main.MainViewController
import com.kostyarazboynik.todoapp.ui.view.fragments.main.MainViewModel

class MainFragmentViewComponent (
    binding: FragmentMainBinding,
    viewModel: MainViewModel,
    navController: NavController,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    layoutInflater: LayoutInflater
) {

    val mainViewController = MainViewController(
        binding = binding,
        viewModel = viewModel,
        navController = navController,
        context = context,
        lifecycleOwner = lifecycleOwner,
        layoutInflater = layoutInflater
    )
}