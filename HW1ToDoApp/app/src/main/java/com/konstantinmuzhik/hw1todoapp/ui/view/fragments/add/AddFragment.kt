package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.konstantinmuzhik.hw1todoapp.App
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.ui.ViewModelFactory
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAddBinding
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.SharedViewModel
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding

    private val mToDoViewModel: ToDoItemViewModel by viewModels{
        ViewModelFactory{
            (requireActivity().application as App).getAppComponent().toDoItemViewModel()
        }
    }
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var currentToDoItem = ToDoItem()

    private var deadlineDate: Date? = null
    private var deadlineChanged = false

    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        createListeners()

        return binding.root
    }

    private fun createListeners() {

        binding.prioritySp.onItemSelectedListener = mSharedViewModel.spinnerListener

        binding.buttonSaveCreate.setOnClickListener {
            createToDoItem()
        }

        binding.toolbar.setNavigationOnClickListener {
            mToDoViewModel.clearToDoItem()
            findNavController().popBackStack()
        }

        datePicker.addOnNegativeButtonClickListener {
            if (deadlineDate == null) deleteDate()
        }
        datePicker.addOnCancelListener {
            if (deadlineDate == null) deleteDate()
        }

        datePicker.addOnPositiveButtonClickListener {
            createDate(it)
        }

        binding.deadlineSw.setOnClickListener {
            if (binding.deadlineSw.isChecked) {
                binding.deadlineDate.visibility = View.VISIBLE
                binding.deadlineDate.text =
                    SimpleDateFormat("d MMMM y", Locale.getDefault()).format(Date())
                showDateTimePicker()
            } else deleteDate()
        }
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
        datePicker.show(requireActivity().supportFragmentManager, "materialDatePicker")

    private fun parsePriorityString(a: String): Priority =
        when (a) {
            getString(R.string.high_priority) -> Priority.HIGH
            getString(R.string.low_priority) -> Priority.LOW
            else -> Priority.NO
        }

    private fun createToDoItem() {
        val title = binding.titleEt.text.toString()
        val time = Calendar.getInstance().time

        currentToDoItem.setArgs(
            title = title,
            priority = parsePriorityString(binding.prioritySp.selectedItem.toString()),
            deadline = if (deadlineChanged) deadlineDate else null,
            done = false,
            changeDate = time
        )

        if (mSharedViewModel.verifyDataFromUser(currentToDoItem.title)) {
            if (mToDoViewModel.status.value == ConnectivityObserver.Status.Available)
                mToDoViewModel.updateRemoteToDoItem(currentToDoItem)
//            else {
//                Toast.makeText(
//                    context,
//                    R.string.no_network_will_be_created_later,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
            currentToDoItem.id = UUID.randomUUID().toString()
            mToDoViewModel.createToDoItem(currentToDoItem)

            Toast.makeText(
                requireContext(), getString(R.string.successfully_added), Toast.LENGTH_SHORT
            ).show()

            mToDoViewModel.clearToDoItem()

            findNavController().navigate(R.id.action_addFragment_to_listFragment)

        } else
            Toast.makeText(requireContext(), getString(R.string.fill_title), Toast.LENGTH_SHORT)
                .show()
    }
}