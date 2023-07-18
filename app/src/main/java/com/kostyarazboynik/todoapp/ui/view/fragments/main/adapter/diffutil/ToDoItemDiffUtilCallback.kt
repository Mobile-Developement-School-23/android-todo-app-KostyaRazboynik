package com.kostyarazboynik.todoapp.ui.view.fragments.main.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.kostyarazboynik.todoapp.data.models.ToDoItem

/**
 * ToDoItem DiffUtil Callback
 *
 * @author Kovalev Konstantin
 *
 */
class ToDoItemDiffUtilCallback : DiffUtil.ItemCallback<ToDoItem>() {

    override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean =
        oldItem == newItem
}