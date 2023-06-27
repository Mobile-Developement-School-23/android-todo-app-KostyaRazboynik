package com.konstantinmuzhik.hw1todoapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.databinding.ToDoItemLayoutBinding

class ToDoAdapter: RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    private var toDoItems = emptyList<ToDoItem>()

    class MyViewHolder(private val binding: ToDoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(toDoItem: ToDoItem) {
            binding.toDoItem = toDoItem
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ToDoItemLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(toDoItems[position])
    }

    override fun getItemCount(): Int {
        return toDoItems.size
    }

    fun setData(mToDoItems: List<ToDoItem>) {
        val toDoDiffUtil = ToDoItemDiffUtil(toDoItems, mToDoItems)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.toDoItems = mToDoItems
        toDoDiffResult.dispatchUpdatesTo(this)
    }

    fun getElement(position: Int): ToDoItem {
        return toDoItems[position]
    }
}