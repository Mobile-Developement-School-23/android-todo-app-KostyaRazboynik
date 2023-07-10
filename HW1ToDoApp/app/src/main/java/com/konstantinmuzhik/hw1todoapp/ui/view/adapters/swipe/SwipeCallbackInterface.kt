package com.konstantinmuzhik.hw1todoapp.ui.view.adapters.swipe

import androidx.recyclerview.widget.RecyclerView
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem

/**
 * Interface for Swipe Callback
 *
 * @author Kovalev Konstantin
 *
 */
interface SwipeCallbackInterface {
    fun onDelete(todoItem: ToDoItem)
    fun onChangeDone(todoItem: ToDoItem)
}