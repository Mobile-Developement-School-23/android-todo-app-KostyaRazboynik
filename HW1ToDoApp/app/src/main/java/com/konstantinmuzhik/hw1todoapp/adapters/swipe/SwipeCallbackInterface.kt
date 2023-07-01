package com.konstantinmuzhik.hw1todoapp.adapters.swipe

import androidx.recyclerview.widget.RecyclerView
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem

interface SwipeCallbackInterface {
    fun onDelete(viewHolder: RecyclerView.ViewHolder, todoItem: ToDoItem)
    fun onChangeDone(todoItem: ToDoItem)
}