package com.konstantinmuzhik.hw1todoapp.ui.view.fragments.add.compose

import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import java.util.Date


sealed class AddFragmentComposeAction {
    data class UpdateText(val text: String) : AddFragmentComposeAction()
    data class UpdatePriority(val priority: Priority) : AddFragmentComposeAction()
    data class UpdateDeadline(val deadline: Date?) : AddFragmentComposeAction()

    object Close : AddFragmentComposeAction()
    object Save : AddFragmentComposeAction()
}
