package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.appComponent
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentUpdateBinding
import com.konstantinmuzhik.hw1todoapp.domain.models.UiState
import com.konstantinmuzhik.hw1todoapp.ui.viewmodels.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.ui.utils.SharedViewHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


/**
 * Update Fragment
 *
 * @author Kovalev Konstantin
 *
 */
class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val mSharedViewHelper = SharedViewHelper
    private val viewModel: ToDoItemViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    private val args: UpdateFragmentArgs by navArgs()

    private var deadlineDate: Date? = null
    private var deadlineChanged = false

    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentUpdateBinding.inflate(layoutInflater)
        binding.args = args

        viewModel.getToDoItem(args.currentItem.id)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoItem.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            binding.currentDeadlineDate.text = if (args.currentItem.deadline != null) SimpleDateFormat(
                                "dd-MM-yyyy",
                                Locale.getDefault()
                            ).format(args.currentItem.deadline!!) else ""
                            createListeners(state.data)
                        }

                        else -> {}
                    }
                }
            }
        }

        return binding.root
    }

    private fun createListeners(toDoItem: ToDoItem) {

        binding.currentPrioritySp.onItemSelectedListener = mSharedViewHelper.spinnerListener

        binding.buttonDeleteTask.setOnClickListener {
            confirmItemRemoval(toDoItem)
        }

        binding.saveBtn.setOnClickListener {
            updateToDoItem(toDoItem)
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

        datePicker.addOnCancelListener {
            if (toDoItem.deadline == null) deleteDate()
        }

        binding.currentDeadlineSw.setOnClickListener {
            if (binding.currentDeadlineSw.isChecked) {
                binding.currentDeadlineDate.visibility = View.VISIBLE
                binding.currentDeadlineDate.text =
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


    private fun confirmItemRemoval(toDoItem: ToDoItem) =
        AlertDialog.Builder(
            ContextThemeWrapper(requireContext(), R.style.AlertDialogCustom)
        ).apply {
            setPositiveButton(getString(R.string.yes_pos_btn)) { _, _ ->

                viewModel.deleteToDoItem(toDoItem)

                makeToast("${getString(R.string.successfully_removed)}: '${toDoItem.title}'")

                findNavController().popBackStack()
            }
            setNegativeButton(getString(R.string.no_neg_btn)) { _, _ -> }
            setTitle("${getString(R.string.delete_warning_title)} '${toDoItem.title}'")
            setMessage("${getString(R.string.delete_warning)} '${toDoItem.title}'?")
            create()
            show()
        }

    private fun parsePriorityString(a: String): Priority =
        when (a) {
            getString(R.string.high_priority) -> Priority.HIGH
            getString(R.string.low_priority) -> Priority.LOW
            else -> Priority.NO
        }

    private fun updateToDoItem(toDoItem: ToDoItem) {
        val title = binding.currentTitleEt.text.toString()
        val time = Calendar.getInstance().time

        toDoItem.setArgs(
            title = title,
            priority = parsePriorityString(binding.currentPrioritySp.selectedItem.toString()),
            deadline = if (deadlineChanged) deadlineDate else args.currentItem.deadline,
            done = args.currentItem.done,
            changeDate = time
        )

        if (mSharedViewHelper.verifyDataFromUser(title)) {
            viewModel.updateToDoItem(toDoItem)

            makeToast(getString(R.string.successfully_updated))

            findNavController().popBackStack()
        } else makeToast(getString(R.string.fill_title))

    }

    private fun makeToast(text: String) =
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}