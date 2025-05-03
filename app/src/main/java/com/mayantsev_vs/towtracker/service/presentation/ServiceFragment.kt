package com.mayantsev_vs.towtracker.service.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.databinding.FragmentServiceBinding
import com.mayantsev_vs.towtracker.main.utils.DialogManager
import com.mayantsev_vs.towtracker.main.utils.TimeUtils
import com.mayantsev_vs.towtracker.main.utils.showToast
import com.mayantsev_vs.towtracker.service.data.cache.ServiceDBO
import com.mayantsev_vs.towtracker.sl.ViewModelFactory

class ServiceFragment : Fragment(), ServiceAdapter.ClickListener {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ServiceAdapter
    private val servicesViewModel: ServiceViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeServices()

        binding.btnAddService.setOnClickListener {
            DialogManager.showServiceDialog(requireContext(), object : DialogManager.ServiceListener {
                override fun onClick(serviceNameInput: String, servicePriceInput: String) {
                    val service = ServiceDBO(
                        id = null,
                        name = serviceNameInput,
                        price = servicePriceInput,
                        date = TimeUtils.getDate()
                    )
                    showToast(getString(R.string.service_saved))
                    servicesViewModel.insertService(service)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() = with(binding) {
        adapter = ServiceAdapter(this@ServiceFragment)
        rcView.layoutManager = LinearLayoutManager(requireContext())
        rcView.adapter = adapter
    }

    private fun observeServices() {
        servicesViewModel.services.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.tvEmptyServices.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onClick(service: ServiceDBO, type: ServiceAdapter.ClickType) {
        when (type) {
            ServiceAdapter.ClickType.DELETE -> servicesViewModel.deleteService(service)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ServiceFragment()
    }
}
