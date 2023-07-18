package com.kostyarazboynik.todoapp.ui.utils


import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.google.android.material.switchmaterial.SwitchMaterial
import com.kostyarazboynik.todoapp.data.models.Importance
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
        fun parsePriorityToInt(spinner: Spinner, importance: Importance) {
            when (importance) {
                Importance.NO -> spinner.setSelection(0)
                Importance.HIGH -> spinner.setSelection(1)
                Importance.LOW -> spinner.setSelection(2)
            }
        }

        @BindingAdapter("android:hasDeadline")
        @JvmStatic
        fun hasDeadline(switch: SwitchMaterial, deadline: Date?) {
            switch.isChecked = deadline != null
        }
    }
}