package com.konstantinmuzhik.hw1todoapp.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentUpdateBinding
import com.konstantinmuzhik.hw1todoapp.factory
import com.konstantinmuzhik.hw1todoapp.fragments.SharedViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoItemViewModel by activityViewModels { factory() }
    private val args by navArgs<UpdateFragmentArgs>()
    private var currentTask = ToDoItem()

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
        binding.args = args

        mToDoViewModel.loadTask(args.currentItem.id)


        lifecycleScope.launch {
            mToDoViewModel.currentItem.collect {
                currentTask = it
                if (currentTask.id != "-1") {
                    createListeners()
                }
            }
        }

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

        //createListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createListeners() {

        binding.currentPrioritySp.onItemSelectedListener = mSharedViewModel.spinnerListener

        binding.buttonDeleteTask.setOnClickListener {
            confirmItemRemoval()
        }

        binding.buttonSaveCreate.setOnClickListener {
            updateToDoItem()
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
        datePicker.show(requireActivity().supportFragmentManager, "materialDatePicker")


    private fun confirmItemRemoval() =
        AlertDialog.Builder(requireContext()).apply {
            setPositiveButton(getString(R.string.yes_pos_btn)) { _, _ ->
                mToDoViewModel.deleteToDoItem(args.currentItem)
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
            show()
        }

    private fun parsePriorityString(a: String): Priority =
       when (a) {
            getString(R.string.high_priority) -> Priority.HIGH
            getString(R.string.low_priority) -> Priority.LOW
            else -> Priority.NO
        }

    private fun updateToDoItem() {
        val name = binding.currentTitleEt.text.toString()



        if (mSharedViewModel.verifyDataFromUser(name)) {
            mToDoViewModel.updateToDoItem(
                ToDoItem(
                    id = args.currentItem.id,
                    title = name,
                    priority = parsePriorityString(binding.currentPrioritySp.selectedItem.toString()),
                    deadline =  if (deadlineChanged) deadlineDate else args.currentItem.deadline,
                    done = args.currentItem.done,
                    creationDate = args.currentItem.creationDate,
                    changeDate = Calendar.getInstance().time
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
}