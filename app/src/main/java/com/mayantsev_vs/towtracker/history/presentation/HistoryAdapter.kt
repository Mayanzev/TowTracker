package com.mayantsev_vs.towtracker.history.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mayantsev_vs.towtracker.databinding.HistoryItemBinding

class HistoryAdapter() : ListAdapter<HistoryUiItem, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HistoryViewHolder(
            HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HistoryViewHolder) {
            holder.bind(getItem(position))
        }
    }

    class HistoryViewHolder(private val binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(historyItem: HistoryUiItem) = with(binding) {
            tvPrice.text = historyItem.price
            tvDate.text = historyItem.date
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<HistoryUiItem>() {
        override fun areItemsTheSame(oldItem: HistoryUiItem, newItem: HistoryUiItem): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: HistoryUiItem, newItem: HistoryUiItem): Boolean {
            return oldItem == newItem
        }
    }

}