package com.konstantinmuzhik.hw1todoapp.fragments.add


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAddBinding
import com.konstantinmuzhik.hw1todoapp.factory
import com.konstantinmuzhik.hw1todoapp.fragments.SharedViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem as ToDoItem


class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val mToDoViewModel: ToDoItemViewModel by activityViewModels { factory() }
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createListeners() {

        binding.prioritySp.onItemSelectedListener = mSharedViewModel.spinnerListener

        binding.buttonSaveCreate.setOnClickListener {
            createToDoItem()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        datePicker.addOnNegativeButtonClickListener {
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
            } else {
                deleteDate()
            }
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
        val name = binding.titleEt.text.toString()
        val time = Calendar.getInstance().time

        if (mSharedViewModel.verifyDataFromUser(name)) {
            mToDoViewModel.createItem(
                ToDoItem(
                    id = LocalDateTime.now().toString(),
                    title = name,
                    priority = parsePriorityString(binding.prioritySp.selectedItem.toString()),
                    deadline = if (deadlineChanged) deadlineDate else null,
                    done = false,
                    creationDate = time,
                    changeDate = time
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