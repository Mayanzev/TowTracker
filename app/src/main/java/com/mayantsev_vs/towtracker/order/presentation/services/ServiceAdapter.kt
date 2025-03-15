package com.mayantsev_vs.towtracker.order.presentation.services

import com.mayantsev_vs.towtracker.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mayantsev_vs.towtracker.order.data.cache.ServiceItem
import com.mayantsev_vs.towtracker.databinding.ServiceItemBinding


// adapter for displaying a list of services using DiffUtil for efficient updates
class ServiceAdapter(private val listener: Listener) : ListAdapter<ServiceItem, ServiceAdapter.Holder>(Comparator()) {

    class Holder(view: View, private val listener: Listener) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val binding = ServiceItemBinding.bind(view)
        private var serviceTemp: ServiceItem? = null

        init {
            binding.ivDelete.setOnClickListener(this)
        }

        fun bind(service: ServiceItem) = with(binding) {
            serviceTemp = service
            tvServiceName.text = service.name
            tvPrice.text = service.price
            tvDate.text = service.date
        }

        override fun onClick(v: View?) {
            serviceTemp?.let { listener.onClick(it) }
        }

    }

    class Comparator : DiffUtil.ItemCallback<ServiceItem>() {
        override fun areItemsTheSame(oldItem: ServiceItem, newItem: ServiceItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ServiceItem, newItem: ServiceItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_item, parent, false)
        return Holder(view, listener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    interface Listener {
        fun onClick(service: ServiceItem)
    }

}