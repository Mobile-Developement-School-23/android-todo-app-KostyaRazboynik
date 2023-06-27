package com.konstantinmuzhik.hw1todoapp.fragments.main

import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.adapters.ToDoAdapter
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentMainBinding
import com.konstantinmuzhik.hw1todoapp.fragments.SharedViewModel
import com.konstantinmuzhik.hw1todoapp.utils.hideKeyboard
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: ToDoAdapter

    private val mToDoViewModel: ToDoItemViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private var isSwitched = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mode", isSwitched)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            isSwitched = savedInstanceState.getBoolean("mode")
            when (isSwitched) {
                true -> {
                    binding.visibility.setImageResource(R.drawable.visibility_off)
                }

                false -> {
                    binding.visibility.setImageResource(R.drawable.visibility)
                }
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        hideKeyboard(requireActivity())

        listAdapter = ToDoAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.visibility.setOnClickListener {
            if (isSwitched) {
                isSwitched = false
                //YoYo.with(Techniques.ZoomIn).playOn(binding.visibility)
                binding.visibility.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.visibility
                    )
                )
            } else {
                isSwitched = true
                //YoYo.with(Techniques.ZoomIn).playOn(binding.visibility)
                binding.visibility.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.visibility_off
                    )
                )
            }
            //mToDoViewModel.getTasks(isSwitched)
            binding.recyclerView.scrollToPosition(0)
        }

        setUpRecyclerView()
        mToDoViewModel.getAllData.observe(viewLifecycleOwner) { data ->
            mSharedViewModel.isDatabaseEmpty(data)
            listAdapter.setData(data)
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
    }

    private fun swipes() {
        val simpleCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val item = listAdapter.getElement(position)
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        mToDoViewModel.deleteItem(item)
                        listAdapter.notifyItemRemoved(viewHolder.absoluteAdapterPosition)
                        restoreDeletedItem(viewHolder.itemView, item)
                    }

                    ItemTouchHelper.RIGHT -> {
                        mToDoViewModel.updateData(
                            ToDoItem(
                                id = item.id,
                                title = item.title,
                                priority = item.priority,
                                deadline = item.deadline,
                                done = !item.done,
                                creationDate = item.creationDate,
                                changeDate = item.changeDate
                            )
                        )
                        listAdapter.notifyItemChanged(position)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    requireContext(),
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_light_red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_light_green
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.baseline_check)
                    .setActionIconTint(
                        ContextCompat.getColor(
                            recyclerView.context,
                            android.R.color.white
                        )
                    )
                    .create()
                    .decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun restoreDeletedItem(view: View, deletedItem: ToDoItem) {
        Snackbar.make(
            view,
            "${getString(R.string.successfully_removed)} '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        ).apply {
            setAction(getString(R.string.undo)) {
                mToDoViewModel.insertData(deletedItem)
            }
            show()
        }
    }
}