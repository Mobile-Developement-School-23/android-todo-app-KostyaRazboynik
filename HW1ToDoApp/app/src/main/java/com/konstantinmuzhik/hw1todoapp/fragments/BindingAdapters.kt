package com.konstantinmuzhik.hw1todoapp.fragments

import android.app.Application
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.fragments.main.MainFragmentDirections
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class BindingAdapters {

    companion object {
        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun navigateToAddFragment(view: RelativeLayout, currentItem: ToDoItem) {
            view.setOnClickListener {
                val action = MainFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun navigateToAddFragment(textView: MaterialTextView, currentItem: ToDoItem) {
            textView.setOnClickListener {
                val action = MainFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                textView.findNavController().navigate(action)
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                else -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(spinner: Spinner, priority: Priority) {
            when (priority) {
                Priority.NO -> spinner.setSelection(0)
                Priority.HIGH -> spinner.setSelection(1)
                Priority.LOW -> spinner.setSelection(2)
            }
        }

        @BindingAdapter("android:hasDeadline")
        @JvmStatic
        fun hasDeadline(switch: SwitchMaterial, deadline: Date?) {
            switch.isChecked = deadline != null
        }

        @BindingAdapter("android:parsePriorityBackground")
        @JvmStatic
        fun parsePriorityBackground(checkBox: CheckBox, priority: Priority) {
            when (priority) {
                Priority.HIGH -> checkBox.buttonTintList =
                    ColorStateList.valueOf(checkBox.context.getColor(R.color.color_light_red))

                Priority.NO -> checkBox.buttonTintList =
                    ColorStateList.valueOf(checkBox.context.getColor(R.color.color_light_gray))

                Priority.LOW -> checkBox.buttonTintList =
                    ColorStateList.valueOf(checkBox.context.getColor(R.color.color_light_green))
            }
        }

        @BindingAdapter("android:parseTitleCardTextColor")
        @JvmStatic
        fun parseTitleCardTextColor(textView: MaterialTextView, done: Boolean) {
            if (done) {
                textView.setTextColor(textView.context.getColor(R.color.color_light_gray))
                textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                textView.setTextColor(textView.context.getColor(R.color.black))
                textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            }
        }

        @BindingAdapter("android:parseTitleCardTextColor")
        @JvmStatic
        fun hasDate(textView: MaterialTextView, toDoItem: ToDoItem) {
            if (toDoItem.deadline != null && !toDoItem.done) {
                textView.visibility = View.VISIBLE
                textView.text =
                    textView.context.getString(
                        R.string.doItBefore,
                        toDoItem.deadline.let {
                            SimpleDateFormat(
                                "d MMMM",
                                Locale.getDefault()
                            ).format(it)
                        })
            } else {
                textView.visibility = View.GONE
            }
        }


        @BindingAdapter("android:changeDoneFlag")
        @JvmStatic
        fun changeDoneFlag(checkBox: MaterialCheckBox, toDoItem: ToDoItem) {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                ToDoItemViewModel(application = Application()).updateData(
                    ToDoItem(
                        id = toDoItem.id,
                        title = toDoItem.title,
                        priority = toDoItem.priority,
                        deadline = toDoItem.deadline,
                        done = isChecked,
                        creationDate = toDoItem.creationDate,
                        changeDate = toDoItem.changeDate
                    )
                )
            }
        }


    }
}