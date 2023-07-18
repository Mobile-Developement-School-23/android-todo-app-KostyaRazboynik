package com.kostyarazboynik.todoapp.ui.view.fragments.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kostyarazboynik.todoapp.ui.view.fragments.main.adapter.diffutil.ToDoItemDiffUtilCallback
import com.kostyarazboynik.todoapp.data.models.ToDoItem
import com.kostyarazboynik.todoapp.databinding.ToDoItemLayoutBinding

/**
 * ToDoItem Adapter
 *
 * @author Kovalev Konstantin
 *
 */
class ToDoAdapter(
    private val onItemChecked: (ToDoItem) -> Unit
) : ListAdapter<ToDoItem, ToDoItemViewHolder>(ToDoItemDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder =
        ToDoItemViewHolder(
            ToDoItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) =
        holder.bind(getItem(position), onItemChecked)
}