package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAddBinding
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.SharedViewHelper
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class AddViewController(
    private val binding: FragmentAddBinding,
    private val mSharedViewHelper: SharedViewHelper,
    private val mToDoViewModel: ToDoItemViewModel,
    private val navController: NavController,
    private val fragment: Fragment,
) {

    private var currentToDoItem = ToDoItem()

    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()

    private var deadlineDate: Date? = null
    private var deadlineChanged = false

    fun setUpViews() {
        setUpPrioritySpinner()
        setUpSaveButton()
        setUpToolBar()
        setUpDatePicker()
        setUpDeadlineSwitch()
    }

    private fun setUpPrioritySpinner() {
        binding.prioritySp.onItemSelectedListener = mSharedViewHelper.spinnerListener
    }

    private fun setUpSaveButton() =
        binding.saveBtn.setOnClickListener {
            createToDoItem()
        }

    private fun setUpToolBar() =
        binding.toolbar.setNavigationOnClickListener {
            mToDoViewModel.clearTask()
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
        datePicker.show(fragment.requireActivity().supportFragmentManager, "materialDatePicker")

    private fun createToDoItem() {
        val title = binding.titleEt.text.toString()
        val time = Calendar.getInstance().time

        setToDoItem(UUID.randomUUID().toString(), title, time)

        if (mSharedViewHelper.verifyDataFromUser(currentToDoItem.title)) createVerifiedToDoItem()
        else makeToast(fragment.getString(R.string.fill_title))
    }

    private fun setToDoItem(id: String, title: String, time: Date) {
        currentToDoItem = ToDoItem(
            id = id,
            title = title,
            priority = parsePriorityString(binding.prioritySp.selectedItem.toString()),
            deadline = if (deadlineChanged) deadlineDate else null,
            done = false,
            changedAt = time,
            createdAt = time
        )
    }

    private fun createVerifiedToDoItem() {
        if (mToDoViewModel.status.value == ConnectivityObserver.Status.Available)
            mToDoViewModel.updateRemoteTask(currentToDoItem)
            else makeToast(fragment.getString(R.string.no_network_will_be_created_later))
        mToDoViewModel.createTask(currentToDoItem)

        makeToast(fragment.getString(R.string.successfully_added))

        mToDoViewModel.clearTask()
        navController.navigate(R.id.action_addFragment_to_listFragment)
    }

    private fun parsePriorityString(a: String): Priority =
        when (a) {
            fragment.getString(R.string.high_priority) -> Priority.HIGH
            fragment.getString(R.string.low_priority) -> Priority.LOW
            else -> Priority.NO
        }

    private fun makeToast(text: String) =
        Toast.makeText(fragment.requireContext(), text, Toast.LENGTH_SHORT).show()
}