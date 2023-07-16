package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.main.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem

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