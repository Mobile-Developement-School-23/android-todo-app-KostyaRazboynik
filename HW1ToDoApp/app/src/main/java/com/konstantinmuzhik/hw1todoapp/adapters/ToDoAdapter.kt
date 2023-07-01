package com.konstantinmuzhik.hw1todoapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.konstantinmuzhik.hw1todoapp.adapters.diffutil.ToDoItemDiffUtilCallback
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.databinding.ToDoItemLayoutBinding

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