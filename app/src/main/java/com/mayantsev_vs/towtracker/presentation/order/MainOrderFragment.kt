package com.mayantsev_vs.towtracker.presentation.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mayantsev_vs.towtracker.databinding.FragmentMainOrderBinding
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.data.db.ServiceItem
import com.mayantsev_vs.towtracker.data.db.TrackItem
import com.mayantsev_vs.towtracker.data.location.LocationService
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import com.mayantsev_vs.towtracker.data.utils.openFragment
import com.mayantsev_vs.towtracker.presentation.CurrentScreen
import com.mayantsev_vs.towtracker.presentation.MainActivity
import com.mayantsev_vs.towtracker.presentation.map.MapFragment
import com.mayantsev_vs.towtracker.presentation.order.services.ServiceViewModel
import com.mayantsev_vs.towtracker.presentation.order.services.ServicesFragment
import com.mayantsev_vs.towtracker.presentation.order.tracks.TrackViewModel
import com.mayantsev_vs.towtracker.presentation.order.tracks.TracksFragment
import com.mayantsev_vs.towtracker.sl.MainApp
import java.math.BigDecimal
import java.util.Locale
import kotlin.getValue


class MainOrderFragment : Fragment() {
    private val fragList = listOf(
        TracksFragment.Companion.newInstance(),
        ServicesFragment.Companion.newInstance()
    )
    private lateinit var binding: FragmentMainOrderBinding
    private val servicesViewModel: ServiceViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }
    private val tracksViewModel: TrackViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }
    private val orderViewModel: OrderViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        observeData()
        onCompleteOrderClick()
        observeOrderState()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(this, fragList)
        binding.viewPager.adapter = adapter

        val tabTitles = resources.getStringArray(R.array.fragments_list_titles)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = tabTitles[pos]
        }.attach()
    }

    private fun observeData() {
        servicesViewModel.services.observe(viewLifecycleOwner) { serviceList ->
            updateTotalPrice(serviceList, tracksViewModel.tracks.value ?: emptyList())
        }

        tracksViewModel.tracks.observe(viewLifecycleOwner) { trackList ->
            updateTotalPrice(servicesViewModel.services.value ?: emptyList(), trackList)
        }
    }

    // An adapter for ViewPager2 that is responsible for creating and managing slices
    class ViewPagerAdapter(fragment: Fragment, private val list: List<Fragment>) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return list.size
        }
        override fun createFragment(position: Int): Fragment {
            return list[position]
        }
    }

    // Calculates the total price by summing up the prices of services and tracks, then updates the UI
    private fun updateTotalPrice(serviceList: List<ServiceItem>, trackList: List<TrackItem>) {
        val totalServicePrice = serviceList.sumOf { it.price.toBigDecimalOrNull() ?: BigDecimal.ZERO }
        val totalTrackPrice = trackList.sumOf { it.price.toBigDecimalOrNull() ?: BigDecimal.ZERO }
        val totalPrice = totalServicePrice + totalTrackPrice
        val formattedPrice = String.format(Locale.US, "%.2f", totalPrice)
        binding.tvTotalPrice.text = getString(R.string.total_price_label, formattedPrice)
    }

    private fun onCompleteOrderClick() {
        binding.btnCompleteOrder.setOnClickListener {
            if (LocationService.isRunning) {
                orderViewModel.updateFinishOrder(true)
                openFragment(MapFragment.Companion.newInstance())
            } else {
                orderViewModel.emptyOrder()
                servicesViewModel.deleteAllServices()
                tracksViewModel.deleteAllTracks()
                openFragment(NewOrderFragment.Companion.newInstance())
            }
        }
    }

    private fun observeOrderState() {
        orderViewModel.isOrderStarted.observe(viewLifecycleOwner) {
            (requireContext() as MainActivity).changeBottomNavigation(CurrentScreen.ORDER)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = MainOrderFragment()
    }
}
