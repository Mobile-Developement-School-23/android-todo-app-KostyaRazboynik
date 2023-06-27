package com.konstantinmuzhik.hw1todoapp.fragments.add


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAddBinding
import com.konstantinmuzhik.hw1todoapp.fragments.SharedViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem as ToDoItem


class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val mToDoViewModel: ToDoItemViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var deadlineDate: Date? = null
    private var deadlineChanged = false

    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        binding.prioritySp.onItemSelectedListener = mSharedViewModel.spinnerListener

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSaveCreate.setOnClickListener {
            insertDataToDB()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        datePicker.addOnNegativeButtonClickListener {
            if (deadlineDate == null) deleteDate()
        }

        datePicker.addOnPositiveButtonClickListener {
            val date: Date
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            date = calendar.time
            binding.deadlineDate.visibility = View.VISIBLE
            binding.deadlineDate.text =
                SimpleDateFormat("d MMMM y", Locale.getDefault()).format(date)
            deadlineDate = date
            deadlineChanged = true
        }

        binding.deadlineSw.setOnClickListener {
            if (binding.deadlineSw.isChecked) {
                binding.deadlineDate.visibility = View.VISIBLE
                binding.deadlineDate.text =
                    SimpleDateFormat("d MMMM y", Locale.getDefault()).format(Date())
                showDateTimePicker()
            } else {
                deleteDate()
            }
        }
    }

    private fun deleteDate() {
        binding.deadlineSw.isChecked = false
        binding.deadlineDate.text = ""
        deadlineDate = null
        deadlineChanged = true
    }

    private fun showDateTimePicker() {
        datePicker.show(requireActivity().supportFragmentManager, "materialDatePicker")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parsePriorityString(a: String): Priority {
        return when (a) {
            getString(R.string.high_priority) -> Priority.HIGH
            getString(R.string.low_priority) -> Priority.LOW
            else -> Priority.NO
        }
    }

    private fun insertDataToDB() {
        val name = binding.titleEt.text.toString()
        val priority = parsePriorityString(binding.prioritySp.selectedItem.toString())
        val deadline = if (deadlineChanged) deadlineDate else null
        val done = false
        val creationDate = Calendar.getInstance().time
        val changeDate = Calendar.getInstance().time

        if (mSharedViewModel.verifyDataFromUser(name)) {
            mToDoViewModel.insertData(
                ToDoItem(
                    id = LocalDateTime.now().toString(),
                    title = name,
                    priority = priority,
                    deadline = deadline,
                    done = done,
                    creationDate = creationDate,
                    changeDate = changeDate
                )
            )

            Toast.makeText(
                requireContext(),
                getString(R.string.successfully_added),
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigate(R.id.action_addFragment_to_listFragment)

        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.fill_title),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}