package com.konstantinmuzhik.hw1todoapp.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.adapters.ToDoAdapter
import com.konstantinmuzhik.hw1todoapp.adapters.swipe.SwipeCallbackInterface
import com.konstantinmuzhik.hw1todoapp.adapters.swipe.SwipeHelper
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentMainBinding
import com.konstantinmuzhik.hw1todoapp.factory
import com.konstantinmuzhik.hw1todoapp.utils.hideKeyboard
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var job: Job? = null

    private val mToDoViewModel: ToDoItemViewModel by activityViewModels { factory() }

    private lateinit var listAdapter: ToDoAdapter

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mode", mToDoViewModel.modeAll)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            when (savedInstanceState.getBoolean("mode")) {
                true -> binding.visibility.setImageResource(R.drawable.visibility_off)
                false -> binding.visibility.setImageResource(R.drawable.visibility)
            }
        }

        mToDoViewModel.getToDoItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mToDoItemViewModel = mToDoViewModel

        hideKeyboard(requireActivity())

        listAdapter = ToDoAdapter(onItemChecked = {
            mToDoViewModel.updateToDoItem(it)
        })
        //binding.addFab.setOnCl

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createListeners()

        setUpRecyclerView()

        lifecycleScope.launch {
            mToDoViewModel.tasks.collectLatest {
                listAdapter.submitList(it)
            }
        }

        job?.cancel()
        job = lifecycleScope.launch {
            mToDoViewModel.countCompletedTask.collectLatest {
                binding.completedTasks.text = getString(R.string.completed_title, it)
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = listAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        swipes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job?.cancel()
        job = null
    }

    private fun swipes() {
        val helper = SwipeHelper(object : SwipeCallbackInterface {
            override fun onDelete(viewHolder: RecyclerView.ViewHolder, todoItem: ToDoItem) {
                mToDoViewModel.deleteToDoItem(todoItem)
                restoreDeletedItem(viewHolder.itemView, todoItem)
            }

            override fun onChangeDone(todoItem: ToDoItem) {
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
                mToDoViewModel.createItem(deletedItem)
            }
            show()
        }
    }

    private fun createListeners() {

        binding.addFab.setOnClickListener {
            binding.addFab.findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        /** REFRESH */
//        binding.swipelayout.setOnRefreshListener {
//            viewModel.loadRemoteTask()
//
//            binding.swipelayout.isRefreshing = false
//        }

        binding.visibility.setOnClickListener {
            mToDoViewModel.changeMode()

            if (mToDoViewModel.modeAll) {
                binding.visibility.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.visibility
                    )
                )
            } else {
                binding.visibility.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.visibility_off
                    )
                )
            }
        }

    }
}