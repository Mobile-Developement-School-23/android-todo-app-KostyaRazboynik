package com.konstantinmuzhik.hw1todoapp.adapters

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.databinding.ToDoItemLayoutBinding
import com.konstantinmuzhik.hw1todoapp.fragments.main.MainFragmentDirections
import java.text.SimpleDateFormat
import java.util.Locale

class ToDoItemViewHolder(private val binding: ToDoItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var todoItem: ToDoItem? = null

    fun bind(toDoItem: ToDoItem, onItemChecked: (ToDoItem) -> Unit) {
        this.todoItem = toDoItem

        binding.checkbox.apply {
            setOnClickListener {
                onItemChecked.invoke(toDoItem)
            }

            isChecked = toDoItem.done

            buttonTintList = when (toDoItem.priority) {
                Priority.HIGH -> ColorStateList.valueOf(this.context.getColor(R.color.color_light_red))
                Priority.NO -> ColorStateList.valueOf(this.context.getColor(R.color.color_light_gray))
                Priority.LOW -> ColorStateList.valueOf(this.context.getColor(R.color.color_light_green))
            }
        }

        binding.title.apply {
            paintFlags = if (toDoItem.done) {
                setTextColor(this.context.getColor(R.color.color_light_gray))
                paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                setTextColor(this.context.getColor(R.color.black))
                paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            setOnClickListener {
                this.findNavController()
                    .navigate(MainFragmentDirections.actionListFragmentToUpdateFragment(toDoItem))
            }

            text = toDoItem.title
        }

        binding.date.apply {
            if (toDoItem.deadline != null && !toDoItem.done) {
                this.visibility = View.VISIBLE
                this.text =
                    this.context.getString(
                        R.string.doItBefore,
                        toDoItem.deadline.let {
                            SimpleDateFormat(
                                "d MMMM",
                                Locale.getDefault()
                            ).format(it!!)
                        })
            } else this.visibility = View.GONE
        }

        binding.relativeLayout.setOnClickListener {
            it.findNavController()
                .navigate(MainFragmentDirections.actionListFragmentToUpdateFragment(toDoItem))
        }
    }
}