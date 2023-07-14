package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.konstantinmuzhik.hw1todoapp.appComponent
import com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose.theme.AppTheme


class AddFragmentCompose : Fragment() {

    private val viewModel: ToDoItemViewModelCompose by viewModels { requireContext().appComponent.findViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        viewModel.setToDoItem()

        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                AppTheme {
                    AddFragmentComposeViewController(
                        viewModel.currentToDoItem,
                        onAction = ::onTodoEditorAction
                    )
                }
            }
        }

        return view
    }

    private fun onTodoEditorAction(action: AddFragmentComposeAction) {
        when (action) {
            AddFragmentComposeAction.Close -> findNavController().popBackStack()

            AddFragmentComposeAction.Save -> {
                viewModel.addToDoItem()
                findNavController().popBackStack()
            }

            is AddFragmentComposeAction.UpdateDeadline -> viewModel.updateDeadline(action.deadline)

            is AddFragmentComposeAction.UpdatePriority -> viewModel.updatePriority(action.priority)

            is AddFragmentComposeAction.UpdateText -> viewModel.updateText(action.text)
        }
    }
}
