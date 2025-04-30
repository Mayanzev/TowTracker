package com.mayantsev_vs.towtracker.history.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mayantsev_vs.towtracker.databinding.HistoryItemBinding

class HistoryAdapter(private val listener: ClickListener) : ListAdapter<HistoryListItem, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HistoryViewHolder(
            HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HistoryViewHolder) {
            holder.bind(getItem(position))
        }
    }

    class HistoryViewHolder(private val binding: HistoryItemBinding, private val listener: ClickListener): RecyclerView.ViewHolder(binding.root) {
        fun bind(historyItem: HistoryListItem) = with(binding) {
            tvPrice.text = historyItem.price
            tvDate.text = historyItem.date
            root.setOnClickListener {
                listener.onClick(historyItem)
            }
        }
    }

    interface ClickListener {
        fun onClick(historyListItem: HistoryListItem)
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<HistoryListItem>() {
        override fun areItemsTheSame(
            oldItem: HistoryListItem,
            newItem: HistoryListItem
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: HistoryListItem,
            newItem: HistoryListItem
        ): Boolean {
            return oldItem == newItem
        }
    }

}