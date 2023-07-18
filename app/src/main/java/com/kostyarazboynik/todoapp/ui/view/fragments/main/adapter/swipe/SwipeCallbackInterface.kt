package com.kostyarazboynik.todoapp.ui.view.fragments.main.adapter.swipe

import com.kostyarazboynik.todoapp.data.models.ToDoItem

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