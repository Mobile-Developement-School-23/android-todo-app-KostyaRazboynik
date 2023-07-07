package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.main

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentMainBinding
import com.konstantinmuzhik.hw1todoapp.ui.view.adapters.ToDoAdapter
import com.konstantinmuzhik.hw1todoapp.ui.view.adapters.swipe.SwipeCallbackInterface
import com.konstantinmuzhik.hw1todoapp.ui.view.adapters.swipe.SwipeHelper
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.data.repository.internet_checker.ConnectivityObserver
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *  Main Fragment View Controller
 *
 *  @author Kovalev Konstantin
 */

class MainViewController(
    private val binding: FragmentMainBinding,
    private val mToDoViewModel: ToDoItemViewModel,
    private val navController: NavController,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
) {

    private lateinit var listAdapter: ToDoAdapter
    private var internetState = ConnectivityObserver.Status.Unavailable

    fun setUpViews() {
        setUpVisibility()
        setUpNetworkState()
        loadLocalData()
        createListeners()
        setUpRecyclerView()
        updateUI()
    }

    private fun loadLocalData() {
        mToDoViewModel.loadLocalData()
    }

    private fun updateUI() {
        lifecycleOwner.lifecycleScope.launch {
            mToDoViewModel.tasks.collectLatest {
                updateUI(it)
            }
        }

        lifecycleOwner.lifecycleScope.launch {
            mToDoViewModel.countCompletedTask.collectLatest {
                binding.completedTasks.text = context.getString(R.string.completed_title, it)
            }
        }
    }

    private fun setUpRecyclerView() {
        listAdapter = ToDoAdapter(onItemChecked = {
            if (internetState == ConnectivityObserver.Status.Available)
                mToDoViewModel.updateRemoteTask(it)
            mToDoViewModel.changeTaskDone(it)
        })

        binding.recyclerView.adapter = listAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        swipes()
    }

    private fun swipes() {
        val helper = SwipeHelper(object : SwipeCallbackInterface {
            override fun onDelete(todoItem: ToDoItem) {
                if (internetState == ConnectivityObserver.Status.Available)
                    mToDoViewModel.deleteRemoteTask(todoItem.id)
                mToDoViewModel.deleteTask(todoItem)
                restoreDeletedItem(todoItem)
            }

            override fun onChangeDone(todoItem: ToDoItem) {
                if (internetState == ConnectivityObserver.Status.Available)
                    mToDoViewModel.updateRemoteTask(todoItem)
                mToDoViewModel.changeTaskDone(todoItem)
            }
        }, context)

        helper.attachToRecyclerView(binding.recyclerView)
    }

    private fun restoreDeletedItem(deletedItem: ToDoItem) =
        makeSnackBar("${context.getString(R.string.successfully_removed)} '${deletedItem.title}'").apply {
            setAction(context.getString(R.string.undo)) {
                if (mToDoViewModel.status.value == ConnectivityObserver.Status.Available)
                    mToDoViewModel.updateRemoteTask(deletedItem)
                mToDoViewModel.createTask(deletedItem)
            }
            show()
        }

    private fun createListeners() {
        addButtonListener()
        swipeRefreshListener()
        logOutButtonListener()
        settingsButtonListener()
        visibilityListener()
    }

    private fun addButtonListener() =
        binding.addFab.setOnClickListener {
            binding.addFab.findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

    private fun swipeRefreshListener() =
        binding.swipeLayout.setOnRefreshListener {
            if (internetState == ConnectivityObserver.Status.Available) {
                mToDoViewModel.loadRemoteList()
                makeSnackBar(context.getString(R.string.merging_data)).show()
            } else makeSnackBar(context.getString(R.string.no_internet_connection)).show()

            binding.swipeLayout.isRefreshing = false
        }

    private fun logOutButtonListener() =
        binding.logoutButton.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setPositiveButton(context.getString(R.string.yes_pos_btn)) { _, _ ->
                    mToDoViewModel.deleteToken()
                    navController.navigate(R.id.action_listFragment_to_fragmentAuth)
                }
                setNegativeButton(context.getString(R.string.no_neg_btn)) { _, _ -> }
                setTitle(context.getString(R.string.log_out_warning_title))
                setMessage(context.getString(R.string.log_out_warning))
                create()
                show()
            }
        }

    private fun settingsButtonListener() =
        binding.settings.setOnClickListener {
            navController.navigate(R.id.action_listFragment_to_settingsFragment)
        }

    private fun visibilityListener() =
        binding.visibility.setOnClickListener {
            mToDoViewModel.changeDone()

            if (mToDoViewModel.modeAll) binding.visibility.setImageDrawable(
                AppCompatResources.getDrawable(context, R.drawable.visibility)
            )
            else binding.visibility.setImageDrawable(
                AppCompatResources.getDrawable(context, R.drawable.visibility_off)
            )
        }

    private fun setUpNetworkState() =
        lifecycleOwner.lifecycleScope.launch {
            mToDoViewModel.status.collectLatest {
                updateNetworkState(it)
            }
        }

    private fun setUpVisibility() =
        when (mToDoViewModel.modeAll) {
            true -> binding.visibility.setImageResource(R.drawable.visibility_off)
            false -> binding.visibility.setImageResource(R.drawable.visibility)
        }

    private fun updateNetworkState(status: ConnectivityObserver.Status) {
        when (status) {
            ConnectivityObserver.Status.Available -> if (internetState != status)
                mToDoViewModel.loadRemoteList()

            else -> makeToast(context.getString(R.string.losing_internet))
        }
        internetState = status
    }

    private fun updateUI(list: List<ToDoItem>) =
        if (mToDoViewModel.modeAll) listAdapter.submitList(list)
        else listAdapter.submitList(list.filter { !it.done })

    private fun makeToast(text: String) = Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

    private fun makeSnackBar(text: String) = Snackbar.make(binding.listLayout, text, Snackbar.LENGTH_LONG)

}