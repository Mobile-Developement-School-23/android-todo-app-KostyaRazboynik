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
import com.google.gson.Gson
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentUpdateBinding
import com.konstantinmuzhik.hw1todoapp.factory
import com.konstantinmuzhik.hw1todoapp.fragments.SharedViewModel
import com.konstantinmuzhik.hw1todoapp.utils.Constants.CURRENT_TO_DO_ITEM
import com.konstantinmuzhik.hw1todoapp.utils.internet_checker.ConnectivityObserver
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoItemViewModel by activityViewModels { factory() }

    private val args: UpdateFragmentArgs by navArgs()
    private var currentToDoItem = ToDoItem()

    private var deadlineDate: Date? = null
    private var deadlineChanged = false

    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUpdateBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.args = args
        val toDoItem = args.currentItem
        if (savedInstanceState == null) {
            mToDoViewModel.loadToDoItem(toDoItem.id)

            lifecycleScope.launch {
                mToDoViewModel.currentItem.collect {
                    currentToDoItem = it
                }
            }
        } else
            currentToDoItem =
                Gson().fromJson(
                    savedInstanceState.getString(CURRENT_TO_DO_ITEM),
                    ToDoItem::class.java
                )

        createListeners()

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

    private fun createListeners() {

        binding.currentPrioritySp.onItemSelectedListener = mSharedViewModel.spinnerListener

        binding.buttonDeleteTask.setOnClickListener {
            confirmItemRemoval()
        }

        binding.buttonSaveCreate.setOnClickListener {
            updateToDoItem()
        }

        binding.toolbar.setNavigationOnClickListener {
            mToDoViewModel.clearToDoItem()
            findNavController().popBackStack()
        }

        datePicker.addOnNegativeButtonClickListener {
            if (deadlineDate == null) deleteDate()
        }

        datePicker.addOnPositiveButtonClickListener {
            createDate(it)
        }

        datePicker.addOnCancelListener {
            if (currentToDoItem.deadline == null) deleteDate()
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


    private fun confirmItemRemoval() =
        AlertDialog.Builder(requireContext()).apply {
            setPositiveButton(getString(R.string.yes_pos_btn)) { _, _ ->
                if (mToDoViewModel.status.value == ConnectivityObserver.Status.Available)
                    mToDoViewModel.deleteRemoteToDoItem(currentToDoItem.id)
//                else Toast.makeText(
//                        context, R.string.no_network_will_be_removed_later, Toast.LENGTH_SHORT
//                    ).show()

                mToDoViewModel.deleteToDoItem(currentToDoItem)
                Toast.makeText(
                    requireContext(),
                    "${getString(R.string.successfully_removed)}: '${currentToDoItem.title}'",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            setNegativeButton(getString(R.string.no_neg_btn)) { _, _ -> }
            setTitle("${getString(R.string.delete_warning_title)} '${currentToDoItem.title}'")
            setMessage("${getString(R.string.delete_warning)} '${currentToDoItem.title}'?")
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
        val title = binding.currentTitleEt.text.toString()
        val time = Calendar.getInstance().time

        currentToDoItem.setArgs(
            title = title,
            priority = parsePriorityString(binding.currentPrioritySp.selectedItem.toString()),
            deadline = if (deadlineChanged) deadlineDate else args.currentItem.deadline,
            done = args.currentItem.done,
            changeDate = time
        )

        if (mSharedViewModel.verifyDataFromUser(title)) {
            if (mToDoViewModel.status.value == ConnectivityObserver.Status.Available)
                mToDoViewModel.createRemoteToDoItem(currentToDoItem)
//            else Toast.makeText(
//                    context, R.string.no_network_will_be_updated_later, Toast.LENGTH_SHORT
//                ).show()

            mToDoViewModel.updateToDoItem(currentToDoItem)

            Toast.makeText(
                requireContext(), getString(R.string.successfully_added), Toast.LENGTH_SHORT
            ).show()

            mToDoViewModel.clearToDoItem()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else Toast.makeText(
            requireContext(), getString(R.string.fill_title), Toast.LENGTH_SHORT
        ).show()
    }
}