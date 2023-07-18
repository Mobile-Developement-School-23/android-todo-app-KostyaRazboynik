package com.kostyarazboynik.todoapp.ui.view.fragments.update

import android.app.AlertDialog
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.data.models.ToDoItem
import com.kostyarazboynik.todoapp.databinding.FragmentUpdateBinding
import com.kostyarazboynik.todoapp.domain.models.UiState
import com.kostyarazboynik.todoapp.ui.utils.SharedViewHelper
import com.kostyarazboynik.todoapp.ui.view.activity.MainActivity
import com.kostyarazboynik.todoapp.ui.viewmodels.ToDoItemViewModel
import com.kostyarazboynik.todoapp.utils.Mappers.toImportance
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Update Fragment View Controller
 *
 * @author Kovalev Konstantin
 */
class UpdateViewController(
    private val binding: FragmentUpdateBinding,
    private val sharedViewHelper: SharedViewHelper,
    private val viewModel: ToDoItemViewModel,
    private val navController: NavController,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {

    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()

    private var deadlineDate: Date? = null
    private var deadlineChanged = false

    private val args = binding.args!!

    fun setUpViewModel() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoItem.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            setUpDeadlineText()
                            setUpPrioritySpinner()
                            setUpUpdateButton(state.data)
                            setUpDeleteButton(state.data)
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

    private fun setUpDeadlineText() {
        binding.currentDeadlineDate.text =
            if (args.currentItem.deadline != null) SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            ).format(args.currentItem.deadline!!) else ""
    }

    private fun setUpPrioritySpinner() {
        binding.currentPrioritySp.onItemSelectedListener = sharedViewHelper.spinnerListener
    }

    private fun setUpUpdateButton(toDoItem: ToDoItem) =
        binding.saveBtn.setOnClickListener {
            updateToDoItem(toDoItem)
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
        binding.currentDeadlineSw.setOnClickListener {
            if (binding.currentDeadlineSw.isChecked) {
                binding.currentDeadlineSw.visibility = View.VISIBLE
                binding.currentDeadlineSw.text =
                    SimpleDateFormat("d MMMM y", Locale.getDefault()).format(Date())
                showDateTimePicker()
            } else deleteDate()
        }

    private fun setUpDeleteButton(toDoItem: ToDoItem) =
        binding.buttonDeleteTask.setOnClickListener {
            confirmItemRemoval(toDoItem)
        }

    private fun createDate(it: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = it
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val date = calendar.time

        binding.currentDeadlineDate.visibility = View.VISIBLE
        binding.currentDeadlineDate.text =
            SimpleDateFormat("d MMMM y", Locale.getDefault()).format(date)

        deadlineDate = date
        deadlineChanged = true
    }

    private fun deleteDate() {
        binding.currentDeadlineSw.isChecked = false
        binding.currentDeadlineDate.text = ""
        deadlineDate = null
        deadlineChanged = true
    }

    private fun showDateTimePicker() =
        datePicker.show((context as MainActivity).supportFragmentManager, "materialDatePicker")

    private fun updateToDoItem(toDoItem: ToDoItem) {
        val title = binding.currentTitleEt.text.toString()

        setToDoItem(toDoItem, title, Calendar.getInstance().time)

        if (sharedViewHelper.verifyDataFromUser(title)) updateVerifiedToDoItem(toDoItem)
        else makeToast(context.getString(R.string.fill_title))
    }

    private fun setToDoItem(toDoItem: ToDoItem, title: String, time: Date) {
        toDoItem.title = title
        toDoItem.importance =
            binding.currentPrioritySp.selectedItem.toString().toImportance(context)
        toDoItem.deadline = if (deadlineChanged) deadlineDate else args.currentItem.deadline
        toDoItem.done = args.currentItem.done
        toDoItem.changedAt = time
    }

    private fun updateVerifiedToDoItem(toDoItem: ToDoItem) {
        viewModel.updateToDoItem(toDoItem)

        makeToast(context.getString(R.string.successfully_updated))

        navController.popBackStack()
    }
    private fun confirmItemRemoval(toDoItem: ToDoItem) =
        AlertDialog.Builder(
            ContextThemeWrapper(context, R.style.AlertDialogCustom)
        ).apply {
            setPositiveButton(context.getString(R.string.yes_pos_btn)) { _, _ ->
                viewModel.deleteToDoItem(toDoItem)

                makeToast("${context.getString(R.string.successfully_removed)}: '${toDoItem.title}'")

                navController.popBackStack()
            }
            setNegativeButton(context.getString(R.string.no_neg_btn)) { _, _ -> }
            setTitle("${context.getString(R.string.delete_warning_title)} '${toDoItem.title}'")
            setMessage("${context.getString(R.string.delete_warning)} '${toDoItem.title}'?")
            create()
            show()
        }

    private fun makeToast(text: String) =
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}
