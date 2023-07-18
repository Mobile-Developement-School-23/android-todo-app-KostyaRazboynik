package com.kostyarazboynik.todoapp.ui.view.fragments.main

import android.app.AlertDialog
import android.content.Context
import android.os.CountDownTimer
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.data.models.ToDoItem
import com.kostyarazboynik.todoapp.databinding.FragmentMainBinding
import com.kostyarazboynik.todoapp.ui.view.fragments.main.adapter.ToDoAdapter
import com.kostyarazboynik.todoapp.ui.view.fragments.main.adapter.swipe.SwipeCallbackInterface
import com.kostyarazboynik.todoapp.ui.view.fragments.main.adapter.swipe.SwipeHelper
import com.kostyarazboynik.todoapp.data.repository.internet_checker.ConnectivityObserver
import com.kostyarazboynik.todoapp.domain.models.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *  Main Fragment View Controller
 *
 *  @author Kovalev Konstantin
 */

// TODO fix sync
class MainViewController(
    private val binding: FragmentMainBinding,
    private val viewModel: MainViewModel,
    private val navController: NavController,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val layoutInflater: LayoutInflater
) {

    private val listAdapter: ToDoAdapter get() = binding.recyclerView.adapter as ToDoAdapter
    private var internetState = viewModel.status.value

    fun setUpViews() {
        createListeners()
        setUpRecyclerView()
        setUpNetworkState()
        loadData()
        updateUI()
    }

    private fun loadData() = viewModel.loadData()

    private fun updateUI() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.visibility.collectLatest { visibilityState ->
                    updateStateUI(visibilityState)
                }
            }
        }

        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countComplete.collectLatest {
                    binding.completedTasks.text = context.getString(R.string.completed_title, it)
                }
            }
        }
        internetState = viewModel.status.value
    }


    private fun createListeners() {
        addButtonListener()
        swipeRefreshListener()
        logOutButtonListener()
        settingsButtonListener()
        visibilityListener()
    }

    private fun setUpNetworkState() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.status.collectLatest {
                    updateNetworkState(it)
                }
            }
        }
    }

    private suspend fun updateStateUI(visibilityState: Boolean) {
        viewModel.toDoItems.collect { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    if (visibilityState) {
                        listAdapter.submitList(uiState.data.sortedBy { it.done })
                        binding.visibility.setImageResource(R.drawable.visibility_off)
                    } else {
                        listAdapter.submitList(uiState.data.filter { !it.done }
                            .sortedBy { it.done })
                        binding.visibility.setImageResource(R.drawable.visibility)
                    }
                }
                else -> {}
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = ToDoAdapter(onItemChecked = {
            viewModel.updateItem(it.copy(done = !it.done))
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        swipes()
    }

    private fun swipes() {
        val helper = SwipeHelper(object : SwipeCallbackInterface {
            override fun onDelete(todoItem: ToDoItem) {
                viewModel.deleteItem(todoItem)
                restoreDeletedItem(todoItem)
            }
            override fun onChangeDone(todoItem: ToDoItem) =
                viewModel.updateItem(todoItem.copy(done = !todoItem.done))

        }, context)

        helper.attachToRecyclerView(binding.recyclerView)
    }

    private fun addButtonListener() =
        binding.addFab.setOnClickListener {
            navController.navigate(R.id.action_listFragment_to_addFragment)
        }

    private fun swipeRefreshListener() =
        binding.swipeLayout.setOnRefreshListener {
            if (internetState == ConnectivityObserver.Status.Available) {
                viewModel.loadNetworkList()

                makeSnackBar(context.getString(R.string.merging_data)).show()
            } else makeSnackBar(context.getString(R.string.no_internet_connection)).show()

            binding.swipeLayout.isRefreshing = false
        }

    private fun logOutButtonListener() =
        binding.logoutButton.setOnClickListener {
            AlertDialog.Builder(
                ContextThemeWrapper(context, R.style.AlertDialogCustom)
            ).apply {
                setPositiveButton(context.getString(R.string.yes_pos_btn)) { _, _ ->
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
            viewModel.changeMode()

            if (viewModel.visibility.value)
                binding.visibility.setImageResource(R.drawable.visibility_off)
            else binding.visibility.setImageResource(R.drawable.visibility)
        }

    private fun updateNetworkState(status: ConnectivityObserver.Status) {
        when (status) {
            ConnectivityObserver.Status.Available -> if (internetState != status) {
                //viewModel.loadNetworkList()
            }
            else -> if (internetState != status) makeToast(context.getString(R.string.lost_internet))
        }
        internetState = status
    }

    private fun makeToast(text: String) = Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

    private fun makeSnackBar(text: String) =
        Snackbar.make(binding.listLayout, text, Snackbar.LENGTH_LONG)

    private fun restoreDeletedItem(todoItem: ToDoItem) {
        val snackBar = Snackbar.make(binding.listLayout, "", Snackbar.LENGTH_INDEFINITE)
        snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        snackBar.view.setBackgroundColor(context.getColor(android.R.color.transparent))

        val customize = layoutInflater.inflate(R.layout.custom_snackbar, null).apply {
            findViewById<TextView>(R.id.title).text =
                context.getString(R.string.delete_task, todoItem.title)
            findViewById<TextView>(R.id.cancel).setOnClickListener {
                viewModel.createItem(todoItem)
                snackBar.dismiss()
            }
        }
        (snackBar.view as Snackbar.SnackbarLayout).addView(customize, 0)
        startSnackBar(customize, snackBar)
    }

    private fun startSnackBar(customize: View, snackBar: Snackbar) {
        val timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000 + 1
                customize.findViewById<TextView>(R.id.timer).text = "$time"
            }
            override fun onFinish() {
                snackBar.dismiss()
            }
        }
        timer.start()
        snackBar.show()
    }
}