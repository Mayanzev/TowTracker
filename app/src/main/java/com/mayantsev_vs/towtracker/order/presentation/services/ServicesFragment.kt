package com.mayantsev_vs.towtracker.order.presentation.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.order.data.cache.ServiceItem
import com.mayantsev_vs.towtracker.main.utils.DialogManager.ServiceListener
import com.mayantsev_vs.towtracker.main.utils.DialogManager.showServiceDialog
import com.mayantsev_vs.towtracker.main.utils.TimeUtils
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import com.mayantsev_vs.towtracker.main.utils.showToast
import com.mayantsev_vs.towtracker.databinding.FragmentServicesBinding
import kotlin.getValue


class ServicesFragment : Fragment(), ServiceAdapter.Listener {
    private lateinit var binding: FragmentServicesBinding
    private var serviceName: String? = null
    private var servicePrice: String? = null
    private var serviceDate: String? = null
    private lateinit var adapter: ServiceAdapter
    private val servicesViewModel: ServiceViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks()
        initRecyclerView()
        getServices()
    }

    private fun setService() {
        showServiceDialog(requireContext(), object : ServiceListener {
            override fun onClick(serviceNameInput: String, servicePriceInput: String) {
                serviceName = serviceNameInput
                servicePrice = servicePriceInput
                serviceDate = TimeUtils.getDate()
                val service = getServiceItem()
                showToast(getString(R.string.service_saved))
                servicesViewModel.insertService(service)
            }
        })
    }

    private fun setOnClicks() = with(binding) {
        val listener = onClicks()
        btnAddService.setOnClickListener(listener)
    }

    private fun onClicks(): View.OnClickListener {
        return View.OnClickListener {
            when (it.id) {
                R.id.btnAddService -> setService()
            }
        }
    }

    private fun getServiceItem(): ServiceItem {
        return ServiceItem(
            null,
            serviceName ?: "",
            servicePrice ?: "",
            serviceDate ?: ""
        )
    }

    private fun initRecyclerView() = with(binding) {
        adapter = ServiceAdapter(this@ServicesFragment)
        rcView.layoutManager = LinearLayoutManager(requireContext())
        rcView.adapter = adapter
    }

    private fun getServices() {
        servicesViewModel.services.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.tvEmptyServices.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onClick(service: ServiceItem) {
        servicesViewModel.deleteService(service)
    }


    companion object {
        @JvmStatic
        fun newInstance() = ServicesFragment()
    }
}