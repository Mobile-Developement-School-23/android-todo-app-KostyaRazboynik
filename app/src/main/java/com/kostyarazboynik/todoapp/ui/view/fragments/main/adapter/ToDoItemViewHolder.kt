package com.kostyarazboynik.todoapp.ui.view.fragments.main.adapter

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.data.models.Importance
import com.kostyarazboynik.todoapp.data.models.ToDoItem
import com.kostyarazboynik.todoapp.databinding.ToDoItemLayoutBinding
import com.kostyarazboynik.todoapp.ui.view.fragments.main.MainFragmentDirections
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * ToDoItem ViewHolder
 *
 * @author Kovalev Konstantin
 *
 */
class ToDoItemViewHolder(private val binding: ToDoItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var todoItem = ToDoItem()

    fun bind(toDoItem: ToDoItem, onItemChecked: (ToDoItem) -> Unit) {
        this.todoItem = toDoItem

        setUpCheckBox(onItemChecked)
        setUpTitle()
        setUpDate()
        setUpCardView()
    }

    private fun setUpCardView() =
        binding.relativeLayout.setOnClickListener {
            it.findNavController()
                .navigate(MainFragmentDirections.actionListFragmentToUpdateFragment(todoItem))
        }

    private fun setUpDate() =
        binding.date.apply {
            if (todoItem.deadline != null && !todoItem.done) {
                this.visibility = View.VISIBLE
                this.text =
                    this.context.getString(
                        R.string.doItBefore,
                        todoItem.deadline.let {
                            SimpleDateFormat(
                                "d MMMM",
                                Locale.getDefault()
                            ).format(it!!)
                        })
            } else this.visibility = View.GONE
        }

    private fun setUpTitle() =
        binding.title.apply {
            paintFlags = if (todoItem.done) {
                setTextColor(this.context.getColor(R.color.color_light_gray))
                paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            setOnClickListener {
                this.findNavController()
                    .navigate(MainFragmentDirections.actionListFragmentToUpdateFragment(todoItem))
            }

            text = todoItem.title
        }

    private fun setUpCheckBox(onItemChecked: (ToDoItem) -> Unit) =
        binding.checkbox.apply {
            setOnClickListener {
                onItemChecked.invoke(todoItem)
            }

            isChecked = todoItem.done

            buttonTintList = when (todoItem.importance) {
                Importance.HIGH -> ColorStateList.valueOf(this.context.getColor(R.color.color_light_red))
                Importance.NO -> ColorStateList.valueOf(this.context.getColor(R.color.color_light_gray))
                Importance.LOW -> ColorStateList.valueOf(this.context.getColor(R.color.color_light_green))
            }
        }
}