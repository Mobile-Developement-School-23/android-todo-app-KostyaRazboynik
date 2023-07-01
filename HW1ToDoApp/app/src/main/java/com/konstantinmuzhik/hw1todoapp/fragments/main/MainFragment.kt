package com.konstantinmuzhik.hw1todoapp.fragments.main

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
import com.konstantinmuzhik.hw1todoapp.adapters.ToDoAdapter
import com.konstantinmuzhik.hw1todoapp.adapters.swipe.SwipeCallbackInterface
import com.konstantinmuzhik.hw1todoapp.adapters.swipe.SwipeHelper
import com.konstantinmuzhik.hw1todoapp.data.models.LoadingState
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentMainBinding
import com.konstantinmuzhik.hw1todoapp.factory
import com.konstantinmuzhik.hw1todoapp.utils.hideKeyboard
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver.Status.Unavailable
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver.Status.Available

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val mToDoViewModel: ToDoItemViewModel by activityViewModels { factory() }

    private lateinit var listAdapter: ToDoAdapter

    private var internetState = Unavailable


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mode", mToDoViewModel.modeAll)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentMainBinding.inflate(layoutInflater)

        when (mToDoViewModel.modeAll) {
            true -> binding.visibility.setImageResource(R.drawable.visibility_off)
            false -> binding.visibility.setImageResource(R.drawable.visibility)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        hideKeyboard(requireActivity())

        lifecycleScope.launch {
            mToDoViewModel.loadingState.collectLatest {
                updateLoadingStatus(it)
            }
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
            if (internetState == Available)
                mToDoViewModel.updateRemoteToDoItem(it)
//            else Toast.makeText(
//                    context,
//                    R.string.no_network_will_be_updated_later,
//                    Toast.LENGTH_SHORT
//                ).show()
            mToDoViewModel.changeToDoItemDone(it)
        })

        binding.recyclerView.adapter = listAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        swipes()
    }

    private fun swipes() {
        val helper = SwipeHelper(object : SwipeCallbackInterface {
            override fun onDelete(viewHolder: RecyclerView.ViewHolder, todoItem: ToDoItem) {
                if (internetState == Available)
                    mToDoViewModel.deleteRemoteToDoItem(todoItem.id)
//                else Toast.makeText(
//                        context,
//                        R.string.no_network_will_be_removed_later,
//                        Toast.LENGTH_SHORT
//                    ).show()
                mToDoViewModel.deleteToDoItem(todoItem)
                restoreDeletedItem(viewHolder.itemView, todoItem)
            }

            override fun onChangeDone(todoItem: ToDoItem) {
                if (internetState == Available)
                    mToDoViewModel.updateRemoteToDoItem(todoItem)
//                else Toast.makeText(
//                        context,
//                        R.string.no_network_will_be_updated_later,
//                        Toast.LENGTH_SHORT
//                    ).show()
                mToDoViewModel.changeToDoItemDone(todoItem)
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
                mToDoViewModel.createToDoItem(deletedItem)
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
                Snackbar.make(requireView(), R.string.merging_data, Snackbar.LENGTH_SHORT)
                    .show()
            } else
                Snackbar.make(requireView(), R.string.no_internet_connection, Snackbar.LENGTH_SHORT)
                    .show()

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

        binding.visibility.setOnClickListener {
            mToDoViewModel.changeMode()

            lifecycleScope.launch {
                mToDoViewModel.tasks.collectLatest {
                    updateUI(it)
                }
            }

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
//                Toast.makeText(context, R.string.internet_connected, Toast.LENGTH_SHORT).show()
                mToDoViewModel.loadRemoteList()
            }

            Unavailable -> if (internetState != status) {
//                Toast.makeText(context, R.string.internet_unavailable, Toast.LENGTH_SHORT).show()
                mToDoViewModel.loadRemoteList()
            }
            else -> Toast.makeText(context, R.string.lost_internet, Toast.LENGTH_SHORT).show()

//            Losing -> if (internetState != status)
//                Toast.makeText(context, R.string.losing_internet, Toast.LENGTH_SHORT).show()
//
//            Lost -> if (internetState != status)
//                Toast.makeText(context, R.string.lost_internet, Toast.LENGTH_SHORT).show()
        }

        internetState = status
    }

    private fun updateLoadingStatus(loadingState: LoadingState<Any>) {
        when (loadingState) {
            is LoadingState.Loading -> binding.recyclerView.visibility = View.GONE
            is LoadingState.Success -> binding.recyclerView.visibility = View.VISIBLE
            is LoadingState.Error -> {
                binding.recyclerView.visibility = View.VISIBLE
//                Toast.makeText(
//                    requireContext(),
//                    R.string.something_went_wrong_showing_local_data,
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }
}