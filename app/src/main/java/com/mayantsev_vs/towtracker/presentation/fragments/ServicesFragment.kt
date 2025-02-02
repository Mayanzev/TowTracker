package com.mayantsev_vs.towtracker.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.data.db.ServiceItem
import com.mayantsev_vs.towtracker.data.utils.DialogManager.ServiceListener
import com.mayantsev_vs.towtracker.data.utils.DialogManager.showServiceDialog
import com.mayantsev_vs.towtracker.data.utils.showToast
import com.mayantsev_vs.towtracker.databinding.FragmentServicesBinding
import com.mayantsev_vs.towtracker.presentation.MainApp
import com.mayantsev_vs.towtracker.presentation.MainViewModel
import kotlin.getValue


class ServicesFragment : Fragment() {
    private lateinit var binding: FragmentServicesBinding
    private var serviceName: String? = null
    private var servicePrice: Double? = null
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
        model.services.observe(viewLifecycleOwner) {
            Log.d("ML", "list size: ${it.size}")
        }
    }

    private fun setService() {
        val service = getServiceItem()
        showServiceDialog(requireContext(), object : ServiceListener {
            override fun onClick(serviceNameInput: String, servicePriceInput: String) {
                serviceName = serviceNameInput
                servicePrice = servicePriceInput.toDouble()
                showToast(getString(R.string.service_saved))
                model.insertService(service)
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
            servicePrice ?: 0.0
        )
    }


    companion object {
        @JvmStatic
        fun newInstance() = ServicesFragment()
    }
}