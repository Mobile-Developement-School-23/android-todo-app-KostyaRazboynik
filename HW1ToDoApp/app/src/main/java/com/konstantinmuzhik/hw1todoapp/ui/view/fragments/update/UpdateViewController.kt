package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.update

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentUpdateBinding
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.SharedViewHelper
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.data.repository.internet_checker.ConnectivityObserver
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
    private val mSharedViewHelper: SharedViewHelper,
    private val mToDoViewModel: ToDoItemViewModel,
    private val navController: NavController,
    private val context: Context,
    private val fragment: Fragment,
    private val currentToDoItem: ToDoItem,
) {

    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()

    private var deadlineDate: Date? = null
    private var deadlineChanged = false

    fun setUpViews() {
        setUpPrioritySpinner()
        setUpUpdateButton()
        setUpToolBar()
        setUpDatePicker()
        setUpDeadlineSwitch()
        setUpDeleteButton()
        setUpDeadlineTextView()
    }

    private fun setUpPrioritySpinner() {
        binding.currentPrioritySp.onItemSelectedListener = mSharedViewHelper.spinnerListener
    }

    private fun setUpUpdateButton() =
        binding.saveBtn.setOnClickListener {
            updateToDoItem()
        }

    private fun setUpToolBar() =
        binding.toolbar.setNavigationOnClickListener {
            //mToDoViewModel.clearToDoItem()
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

    private fun setUpDeleteButton() =
        binding.buttonDeleteTask.setOnClickListener {
            confirmItemRemoval()
        }

    private fun setUpDeadlineTextView() {
        binding.currentDeadlineDate.text = if (currentToDoItem.deadline != null) SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        ).format(currentToDoItem.deadline!!) else ""
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
        datePicker.show(fragment.requireActivity().supportFragmentManager, "materialDatePicker")

    private fun updateToDoItem() {
        val title = binding.currentTitleEt.text.toString()
        val time = Calendar.getInstance().time

        setToDoItem(title, time)

        if (mSharedViewHelper.verifyDataFromUser(currentToDoItem.title)) updateVerifiedToDoItem()
        else makeToast(context.getString(R.string.fill_title))
    }

    private fun setToDoItem(title: String, time: Date) =
        currentToDoItem.setArgs(
            title = title,
            priority = parsePriorityString(binding.currentPrioritySp.selectedItem.toString()),
            deadline = if (deadlineChanged) deadlineDate else currentToDoItem.deadline,
            done = currentToDoItem.done,
            changeDate = time
        )

    private fun updateVerifiedToDoItem() {
        if (mToDoViewModel.status.value == ConnectivityObserver.Status.Available)
            mToDoViewModel.createRemoteTask(currentToDoItem)
        else makeToast(context.getString(R.string.no_network_will_be_updated_later))

        mToDoViewModel.updateTask(currentToDoItem)

        makeToast(context.getString(R.string.successfully_updated))
        mToDoViewModel.clearTask()
        navController.navigate(R.id.action_updateFragment_to_listFragment)
    }

    private fun parsePriorityString(a: String): Priority =
        when (a) {
            context.getString(R.string.high_priority) -> Priority.HIGH
            context.getString(R.string.low_priority) -> Priority.LOW
            else -> Priority.NO
        }

    private fun confirmItemRemoval() =
        AlertDialog.Builder(context).apply {
            setPositiveButton(context.getString(R.string.yes_pos_btn)) { _, _ ->
                if (mToDoViewModel.status.value == ConnectivityObserver.Status.Available)
                    mToDoViewModel.deleteRemoteTask(currentToDoItem.id)
                else makeToast(context.getString(R.string.no_network_will_be_removed_later))

                mToDoViewModel.deleteTask(currentToDoItem)
                makeToast("${context.getString(R.string.successfully_removed)}: '${currentToDoItem.title}'")


                navController.navigate(R.id.action_updateFragment_to_listFragment)
            }
            setNegativeButton(context.getString(R.string.no_neg_btn)) { _, _ -> }
            setTitle("${context.getString(R.string.delete_warning_title)} '${currentToDoItem.title}'")
            setMessage("${context.getString(R.string.delete_warning)} '${currentToDoItem.title}'?")
            create()
            show()
        }

    private fun makeToast(text: String) =
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}
