package com.konstantinmuzhik.hw1todoapp.ui.view.adapters.swipe

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper

class SwipeHelper(
    swipeCallback: SwipeCallbackInterface,
    context: Context
) : ItemTouchHelper(SwipeCallback(swipeCallback, context))