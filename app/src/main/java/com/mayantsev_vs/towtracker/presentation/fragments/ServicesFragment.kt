package com.mayantsev_vs.towtracker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.data.db.ServiceAdapter
import com.mayantsev_vs.towtracker.data.db.ServiceItem
import com.mayantsev_vs.towtracker.data.utils.DialogManager.ServiceListener
import com.mayantsev_vs.towtracker.data.utils.DialogManager.showServiceDialog
import com.mayantsev_vs.towtracker.data.utils.showToast
import com.mayantsev_vs.towtracker.databinding.FragmentServicesBinding
import com.mayantsev_vs.towtracker.presentation.MainApp
import com.mayantsev_vs.towtracker.presentation.MainViewModel
import kotlin.getValue


class ServicesFragment : Fragment(), ServiceAdapter.Listener {
    private lateinit var binding: FragmentServicesBinding
    private var serviceName: String? = null
    private var servicePrice: Double? = null
    private lateinit var adapter: ServiceAdapter
    private val model: MainViewModel by activityViewModels {
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).database)
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

    // shows a dialog to input service details, then saves and inserts the service
    private fun setService() {
        showServiceDialog(requireContext(), object : ServiceListener {
            override fun onClick(serviceNameInput: String, servicePriceInput: String) {
                serviceName = serviceNameInput
                servicePrice = servicePriceInput.toDouble()
                val service = getServiceItem()
                showToast(getString(R.string.service_saved))
                model.insertService(service)
            }
        })
    }

    // binds the created click listener to the corresponding views in the layout using their IDs
    private fun setOnClicks() = with(binding) {
        val listener = onClicks()
        btnAddService.setOnClickListener(listener)
    }

    // creates and returns a click listener that handles multiple button clicks based on their IDs
    private fun onClicks(): View.OnClickListener {
        return View.OnClickListener {
            when (it.id) {
                R.id.btnAddService -> setService()
            }
        }
    }

    // returns a ServiceItem object
    private fun getServiceItem(): ServiceItem {
        return ServiceItem(
            null,
            serviceName ?: "",
            servicePrice ?: 0.0
        )
    }

    // initializes RecyclerView with a LinearLayoutManager and sets the adapter
    private fun initRecyclerView() = with(binding) {
        adapter = ServiceAdapter(this@ServicesFragment)
        rcView.layoutManager = LinearLayoutManager(requireContext())
        rcView.adapter = adapter
    }

    // observes track data and updates the adapter and empty state visibility
    private fun getServices() {
        model.services.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.tvEmptyServices.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onClick(service: ServiceItem) {
        model.deleteService(service)
    }


    companion object {
        @JvmStatic
        fun newInstance() = ServicesFragment()
    }
}