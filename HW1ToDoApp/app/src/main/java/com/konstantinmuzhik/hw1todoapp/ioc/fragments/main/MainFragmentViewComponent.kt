package com.konstantinmuzhik.hw1todoapp.ioc.fragments.main

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.data.datasource.SharedPreferencesAppSettings
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentMainBinding
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.main.MainViewController
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.main.MainViewModel

class MainFragmentViewComponent (
    binding: FragmentMainBinding,
    viewModel: MainViewModel,
    navController: NavController,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    layoutInflater: LayoutInflater
) {

    val mainViewController = MainViewController(
        binding,
        viewModel,
        navController,
        context,
        lifecycleOwner,
        layoutInflater
    )
}