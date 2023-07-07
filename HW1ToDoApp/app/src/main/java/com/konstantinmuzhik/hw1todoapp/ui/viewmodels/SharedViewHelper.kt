package com.konstantinmuzhik.hw1todoapp.ui.viewmodels

import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.konstantinmuzhik.hw1todoapp.R


object SharedViewHelper {

    val spinnerListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View,
            position: Int,
            id: Long
        ) {
            when (position) {
                0 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.color_light_gray
                        )
                    )
                }

                1 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.color_light_red
                        )
                    )
                }

                2 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.color_light_green
                        )
                    )
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    fun verifyDataFromUser(title: String) = title.isNotEmpty()
}