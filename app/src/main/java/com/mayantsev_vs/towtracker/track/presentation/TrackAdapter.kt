package com.mayantsev_vs.towtracker.track.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mayantsev_vs.towtracker.databinding.TrackItemBinding
import com.mayantsev_vs.towtracker.track.data.cache.TrackDBO


class TrackAdapter(private val listener: ClickListener) : ListAdapter<TrackDBO, TrackAdapter.TrackViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TrackViewHolder(private val binding: TrackItemBinding, private val listener: ClickListener) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: TrackDBO) = with(binding) {
            tvDate.text = track.date
            tvTime.text = track.time
            tvDistance.text = track.distance
            tvPrice.text = track.price
            tvCityFrom.text = track.firstCity
            tvCityTo.text = track.secondCity

            item.setOnClickListener {
                listener.onClick(track, ClickType.OPEN)
            }

            ivDelete.setOnClickListener {
                listener.onClick(track, ClickType.DELETE)
            }
        }
    }

    interface ClickListener {
        fun onClick(track: TrackDBO, type: ClickType)
    }

    enum class ClickType {
        DELETE,
        OPEN
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<TrackDBO>() {
        override fun areItemsTheSame(oldItem: TrackDBO, newItem: TrackDBO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TrackDBO, newItem: TrackDBO): Boolean {
            return oldItem == newItem
        }
    }
}
