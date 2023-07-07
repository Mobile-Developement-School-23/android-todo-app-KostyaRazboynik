package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.appComponent
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentMainBinding
import com.konstantinmuzhik.hw1todoapp.ui.view.adapters.ToDoAdapter
import com.konstantinmuzhik.hw1todoapp.ui.view.adapters.swipe.SwipeCallbackInterface
import com.konstantinmuzhik.hw1todoapp.ui.view.adapters.swipe.SwipeHelper
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver.Status.Available
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver.Status.Losing
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver.Status.Lost
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver.Status.Unavailable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    private val mToDoViewModel: ToDoItemViewModel by activityViewModels { requireContext().appComponent.findViewModelFactory() }

    private lateinit var listAdapter: ToDoAdapter
    private var internetState = Unavailable



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mode", mToDoViewModel.modeAll)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        when (mToDoViewModel.modeAll) {
            true -> binding.visibility.setImageResource(R.drawable.visibility_off)
            false -> binding.visibility.setImageResource(R.drawable.visibility)
        }

        lifecycleScope.launch {
            mToDoViewModel.status.collectLatest {
                updateNetworkState(it)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createListeners()

        setUpRecyclerView()

        mToDoViewModel.loadLocalData()

        lifecycleScope.launch {
            mToDoViewModel.tasks.collectLatest {
                updateUI(it)
            }
        }

        lifecycleScope.launch {
            mToDoViewModel.countCompletedTask.collectLatest {
                binding.completedTasks.text = getString(R.string.completed_title, it)
            }
        }
    }

    private fun setUpRecyclerView() {
        listAdapter = ToDoAdapter(onItemChecked = {
            if (internetState == Available) {
                mToDoViewModel.updateRemoteTask(it)
            } else {
                Toast.makeText(
                    context,
                    "No internet connection, will upload with later. Continue offline.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            mToDoViewModel.changeTaskDone(it)
        })

        binding.recyclerView.adapter = listAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        swipes()
    }

    private fun swipes() {
        val helper = SwipeHelper(object : SwipeCallbackInterface {
            override fun onDelete(viewHolder: RecyclerView.ViewHolder, todoItem: ToDoItem) {
                if (internetState == Available) {
                    mToDoViewModel.deleteRemoteTask(todoItem.id)
                } else {
                    Toast.makeText(
                        context,
                        "No internet connection, will upload later. Continue offline.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                mToDoViewModel.deleteTask(todoItem)
                restoreDeletedItem(viewHolder.itemView, todoItem)
            }

            override fun onChangeDone(todoItem: ToDoItem) {
                if (internetState == Available) {
                    mToDoViewModel.updateRemoteTask(todoItem)
                } else {
                    Toast.makeText(
                        context,
                        "No internet connection, will upload later. Continue offline.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                mToDoViewModel.changeTaskDone(todoItem)
            }
        }, requireContext())

        helper.attachToRecyclerView(binding.recyclerView)
    }

    private fun restoreDeletedItem(view: View, deletedItem: ToDoItem) {
        Snackbar.make(
            view,
            "${getString(R.string.successfully_removed)} '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        ).apply {
            setAction(getString(R.string.undo)) {
                if (mToDoViewModel.status.value == ConnectivityObserver.Status.Available)
                    mToDoViewModel.updateRemoteTask(deletedItem)
                mToDoViewModel.createTask(deletedItem)
            }
            show()
        }
    }

    private fun createListeners() {

        binding.addFab.setOnClickListener {
            binding.addFab.findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        binding.swipeLayout.setOnRefreshListener {
            if (internetState == Available) {
                mToDoViewModel.loadRemoteList()
                Snackbar.make(
                    requireView(),
                    R.string.merging_data, Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    requireView(),
                    R.string.no_internet_connection,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            binding.swipeLayout.isRefreshing = false
        }

        binding.logoutButton.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setPositiveButton(getString(R.string.yes_pos_btn)) { _, _ ->
                    mToDoViewModel.deleteToken()
                    findNavController().navigate(R.id.action_listFragment_to_fragmentAuth)
                }
                setNegativeButton(getString(R.string.no_neg_btn)) { _, _ -> }
                setTitle(getString(R.string.log_out_warning_title))
                setMessage(getString(R.string.log_out_warning))
                create()
                show()
            }
        }

        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_settingsFragment)
        }

        binding.visibility.setOnClickListener {
            mToDoViewModel.changeDone()

//            lifecycleScope.launch {
//                mToDoViewModel.tasks.collectLatest {
//                    updateUI(it)
//                }
//            }

            if (mToDoViewModel.modeAll) binding.visibility.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.visibility
                    )
                )
            else binding.visibility.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.visibility_off
                    )
                )
        }
    }

    private fun updateUI(list: List<ToDoItem>) =
        if (mToDoViewModel.modeAll) listAdapter.submitList(list)
        else listAdapter.submitList(list.filter { !it.done })



    private fun updateNetworkState(status: ConnectivityObserver.Status) {
        when (status) {
            Available -> if (internetState != status) {
                    Toast.makeText(context, R.string.merging_data, Toast.LENGTH_SHORT)
                        .show()
                    mToDoViewModel.loadRemoteList()
                }


            Unavailable -> if (internetState != status) {
                    Toast.makeText(context, R.string.internet_unavailable, Toast.LENGTH_SHORT)
                        .show()
                    mToDoViewModel.loadRemoteList()
                }

            Losing -> if (internetState != status)
                    Toast.makeText(context, R.string.losing_internet, Toast.LENGTH_SHORT)
                        .show()

            Lost -> if (internetState != status)
                    Toast.makeText(context, R.string.lost_internet, Toast.LENGTH_SHORT).show()
        }
        internetState = status
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}