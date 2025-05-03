package com.mayantsev_vs.towtracker.service.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mayantsev_vs.towtracker.databinding.ServiceItemBinding
import com.mayantsev_vs.towtracker.service.data.cache.ServiceDBO

class ServiceAdapter(private val listener: ClickListener) :
    ListAdapter<ServiceDBO, ServiceAdapter.ServiceViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ServiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ServiceViewHolder(
        private val binding: ServiceItemBinding,
        private val listener: ClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(service: ServiceDBO) = with(binding) {
            tvServiceName.text = service.name
            tvPrice.text = service.price
            tvDate.text = service.date

            ivDelete.setOnClickListener {
                listener.onClick(service, ClickType.DELETE)
            }
        }
    }

    interface ClickListener {
        fun onClick(service: ServiceDBO, type: ClickType)
    }

    enum class ClickType {
        DELETE
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<ServiceDBO>() {
        override fun areItemsTheSame(oldItem: ServiceDBO, newItem: ServiceDBO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ServiceDBO, newItem: ServiceDBO): Boolean {
            return oldItem == newItem
        }
    }
}
