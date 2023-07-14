package com.konstantinmuzhik.hw1todoapp.ioc.fragments.update

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentUpdateBinding
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.update.UpdateViewController
import com.konstantinmuzhik.hw1todoapp.ui.utils.SharedViewHelper

class UpdateFragmentViewComponent (
    binding: FragmentUpdateBinding,
    mSharedViewModel: SharedViewHelper,
    mToDoViewModel: ToDoItemViewModel,
    navController: NavController,
    fragment: Fragment,
    context: Context,
    toDoItem: ToDoItem
) {

    val updateViewController = UpdateViewController(
        binding,
        mSharedViewModel,
        mToDoViewModel,
        navController,
        context,
        fragment,
        toDoItem
    )
}