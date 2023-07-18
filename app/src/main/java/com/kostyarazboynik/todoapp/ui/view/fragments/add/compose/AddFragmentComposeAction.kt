package com.kostyarazboynik.todoapp.ui.view.fragments.add.compose

import com.kostyarazboynik.todoapp.data.models.Importance
import java.util.Date


sealed class AddFragmentComposeAction {
    data class UpdateText(val text: String) : AddFragmentComposeAction()
    data class UpdatePriority(val importance: Importance) : AddFragmentComposeAction()
    data class UpdateDeadline(val deadline: Date?) : AddFragmentComposeAction()

    object Close : AddFragmentComposeAction()
    object Save : AddFragmentComposeAction()
}
