package com.konstantinmuzhik.hw1todoapp.ui.view.adapters.swipe

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.ui.view.adapters.ToDoItemViewHolder
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

/**
 * Swipe Callback
 *
 * @author Kovalev Konstantin
 *
 */
class SwipeCallback(
    private val swipeCallback: SwipeCallbackInterface,
    private val context: Context,
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.LEFT) setDeleteCallback(viewHolder)
        else if (direction == ItemTouchHelper.RIGHT) setChangeDoneCallback(viewHolder)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean,
    ) {
        setRecyclerViewSwipeDecorator(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun setRecyclerViewSwipeDecorator(
        c: Canvas,
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean,
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.color_light_red))
            .addSwipeLeftActionIcon(R.drawable.delete)
            .addSwipeRightBackgroundColor(
                ContextCompat.getColor(context, R.color.color_light_green)
            )
            .addSwipeRightActionIcon(R.drawable.swipe_check)
            .setActionIconTint(ContextCompat.getColor(recyclerView.context, android.R.color.white))
            .create()
            .decorate()
    }

    private fun setDeleteCallback(viewHolder: RecyclerView.ViewHolder) =
        (viewHolder as? ToDoItemViewHolder)?.todoItem?.let {
            swipeCallback.onDelete(it)
        }

    private fun setChangeDoneCallback(viewHolder: RecyclerView.ViewHolder) =
        (viewHolder as? ToDoItemViewHolder)?.todoItem?.let { swipeCallback.onChangeDone(it) }
}