package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.xml

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAddBinding
import com.konstantinmuzhik.hw1todoapp.domain.models.UiState
import com.konstantinmuzhik.hw1todoapp.ui.view.activity.MainActivity
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.ui.utils.SharedViewHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID


/**
 * Add Fragment View Controller
 *
 * @author Kovalev Konstantin
 *
 */
class AddViewController(
    private val binding: FragmentAddBinding,
    private val mSharedViewHelper: SharedViewHelper,
    private val viewModel: ToDoItemViewModel,
    private val navController: NavController,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {

    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()

    private var deadlineDate: Date? = null
    private var deadlineChanged = false


    fun setUpViewModel() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoItem.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            setUpPrioritySpinner()
                            setUpSaveButton(state.data)
                            setUpToolBar()
                            setUpDatePicker()
                            setUpDeadlineSwitch()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun setUpPrioritySpinner() {
        binding.prioritySp.onItemSelectedListener = mSharedViewHelper.spinnerListener
    }

    private fun setUpSaveButton(toDoItem: ToDoItem) =
        binding.saveBtn.setOnClickListener {
            createToDoItem(toDoItem)
        }

    private fun setUpToolBar() =
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

    private fun setUpDatePicker() =
        datePicker.apply {
            addOnPositiveButtonClickListener {
                createDate(it)
            }
            addOnNegativeButtonClickListener {
                if (deadlineDate == null) deleteDate()
            }
            addOnCancelListener {
                if (deadlineDate == null) deleteDate()
            }
        }

    private fun setUpDeadlineSwitch() =
        binding.deadlineSw.setOnClickListener {
            if (binding.deadlineSw.isChecked) {
                binding.deadlineDate.visibility = View.VISIBLE
                binding.deadlineDate.text =
                    SimpleDateFormat("d MMMM y", Locale.getDefault()).format(Date())
                showDateTimePicker()
            } else deleteDate()
        }

    private fun createDate(it: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = it
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val date = calendar.time

        binding.deadlineDate.visibility = View.VISIBLE
        binding.deadlineDate.text =
            SimpleDateFormat("d MMMM y", Locale.getDefault()).format(date)

        deadlineDate = date
        deadlineChanged = true
    }

    private fun deleteDate() {
        binding.deadlineSw.isChecked = false
        binding.deadlineDate.text = ""
        deadlineDate = null
        deadlineChanged = true
    }

    private fun showDateTimePicker() =
        datePicker.show((context as MainActivity).supportFragmentManager, "materialDatePicker")

    private fun createToDoItem(toDoItem: ToDoItem) {
        val title = binding.titleEt.text.toString()
        val time = Calendar.getInstance().time

        setToDoItem(toDoItem, title, time)

        if (mSharedViewHelper.verifyDataFromUser(title)) createVerifiedToDoItem(toDoItem)
        else makeToast(context.getString(R.string.fill_title))
    }

    private fun setToDoItem(toDoItem: ToDoItem, title: String, time: Date) {
        toDoItem.id = UUID.randomUUID().toString()
        toDoItem.title = title
        toDoItem.priority = parsePriorityString(binding.prioritySp.selectedItem.toString())
        toDoItem.deadline = if (deadlineChanged) deadlineDate else null
        toDoItem.done = false
        toDoItem.changedAt = time
        toDoItem.createdAt = time
    }

    private fun createVerifiedToDoItem(toDoItem: ToDoItem) {
        viewModel.addToDoItem(toDoItem)

        makeToast(context.getString(R.string.successfully_added))

        navController.popBackStack()
    }

    private fun parsePriorityString(a: String): Priority =
        when (a) {
            context.getString(R.string.high_priority) -> Priority.HIGH
            context.getString(R.string.low_priority) -> Priority.LOW
            else -> Priority.NO
        }

    private fun makeToast(text: String) =
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}