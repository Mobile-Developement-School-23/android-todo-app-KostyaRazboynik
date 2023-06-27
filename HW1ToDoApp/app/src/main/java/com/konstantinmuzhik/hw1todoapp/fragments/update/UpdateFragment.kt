package com.konstantinmuzhik.hw1todoapp.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentUpdateBinding
import com.konstantinmuzhik.hw1todoapp.fragments.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*


class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoItemViewModel by viewModels()
    private val args by navArgs<UpdateFragmentArgs>()

    private var deadlineDate: Date? = null
    private var deadlineChanged = false

    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args /* TODO use ViewBinding instead of DataBinding */

        binding.currentPrioritySp.onItemSelectedListener = mSharedViewModel.spinnerListener
        binding.creationDate.text = "${getString(R.string.creation_date)} ${
            SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            ).format(args.currentItem.creationDate)
        }"
        binding.lastChangeDate.text = "${getString(R.string.last_change_date)} ${
            SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            ).format(args.currentItem.changeDate!!)
        }"
        binding.currentDeadlineDate.text = if (args.currentItem.deadline != null) SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        ).format(args.currentItem.deadline!!) else ""

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonDeleteTask.setOnClickListener {
            confirmItemRemoval()
        }

        binding.buttonSaveCreate.setOnClickListener {
            updateItem()
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
            binding.currentDeadlineDate.visibility = View.VISIBLE
            binding.currentDeadlineDate.text =
                SimpleDateFormat("d MMMM y", Locale.getDefault()).format(date)
            deadlineDate = date
            deadlineChanged = true
        }

        binding.currentDeadlineSw.setOnClickListener {
            if (binding.currentDeadlineSw.isChecked) {
                binding.currentDeadlineDate.visibility = View.VISIBLE
                binding.currentDeadlineDate.text =
                    SimpleDateFormat("d MMMM y", Locale.getDefault()).format(Date())
                showDateTimePicker()
            } else {
                deleteDate()
            }
        }

    }

    private fun deleteDate() {
        binding.currentDeadlineSw.isChecked = false
        binding.currentDeadlineDate.text = ""
        deadlineDate = null
        deadlineChanged = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun confirmItemRemoval() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setPositiveButton(getString(R.string.yes_pos_btn)) { _, _ ->
                mToDoViewModel.deleteItem(args.currentItem)
                Toast.makeText(
                    requireContext(),
                    "${getString(R.string.successfully_removed)}: '${args.currentItem.title}'",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            setNegativeButton(getString(R.string.no_neg_btn)) { _, _ -> }
            setTitle("${getString(R.string.delete_warning)} '${args.currentItem.title}'")
            setMessage("${getString(R.string.warning)} '${args.currentItem.title}'?")
            create()
        }

        alertDialog.show()
    }

    private fun parsePriorityString(a: String): Priority {
        return when (a) {
            getString(R.string.high_priority) -> Priority.HIGH
            getString(R.string.low_priority) -> Priority.LOW
            else -> Priority.NO
        }
    }

    private fun updateItem() {
        val name = binding.currentTitleEt.text.toString()
        val priority = parsePriorityString(binding.currentPrioritySp.selectedItem.toString())
        val deadline = if (deadlineChanged) deadlineDate else args.currentItem.deadline
        val done = args.currentItem.done
        val creationDate = args.currentItem.creationDate
        val changeDate = Calendar.getInstance().time
        if (mSharedViewModel.verifyDataFromUser(name)) {
            mToDoViewModel.updateData(
                ToDoItem(
                    id = args.currentItem.id,
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
                getString(R.string.successfully_updated),
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.fill_title),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showDateTimePicker() {
        datePicker.show(requireActivity().supportFragmentManager, "materialDatePicker")
    }
}