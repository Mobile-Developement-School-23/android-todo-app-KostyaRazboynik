package com.konstantinmuzhik.hw1todoapp.ui


import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.google.android.material.switchmaterial.SwitchMaterial
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import java.util.Date

/**
 * Data Binding
 *
 * @author Kovalev Konstantin
 *
 */
class BindingAdapters {

    companion object {

        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(spinner: Spinner, priority: Priority) {
            when (priority) {
                Priority.NO -> spinner.setSelection(0)
                Priority.HIGH -> spinner.setSelection(1)
                Priority.LOW -> spinner.setSelection(2)
            }
        }

        @BindingAdapter("android:hasDeadline")
        @JvmStatic
        fun hasDeadline(switch: SwitchMaterial, deadline: Date?) {
            switch.isChecked = deadline != null
        }
    }
}