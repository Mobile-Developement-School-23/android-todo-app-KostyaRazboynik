package com.kostyarazboynik.todoapp.ui.view.fragments.main.adapter.swipe

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper

/**
 * Swipe Helper
 *
 * @author Kovalev Konstantin
 *
 */
class SwipeHelper(
    swipeCallback: SwipeCallbackInterface,
    context: Context
) : ItemTouchHelper(SwipeCallback(swipeCallback, context))