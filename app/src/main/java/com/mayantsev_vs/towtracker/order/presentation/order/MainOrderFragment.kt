package com.mayantsev_vs.towtracker.order.presentation.order

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.order.data.cache.ServiceItem
import com.mayantsev_vs.towtracker.order.data.cache.TrackItem
import com.mayantsev_vs.towtracker.map.data.location.LocationService
import com.mayantsev_vs.towtracker.main.utils.openParentFragment
import com.mayantsev_vs.towtracker.main.utils.openParentFragmentBackstack
import com.mayantsev_vs.towtracker.databinding.FragmentMainOrderBinding
import com.mayantsev_vs.towtracker.main.presentation.MainFragment.CurrentScreen
import com.mayantsev_vs.towtracker.main.presentation.MainViewModel
import com.mayantsev_vs.towtracker.main.utils.TimeUtils
import com.mayantsev_vs.towtracker.map.presentation.MapFragment
import com.mayantsev_vs.towtracker.map.presentation.MapViewModel
import com.mayantsev_vs.towtracker.order.presentation.services.ServiceViewModel
import com.mayantsev_vs.towtracker.order.presentation.services.ServicesFragment
import com.mayantsev_vs.towtracker.order.presentation.tracks.TrackViewModel
import com.mayantsev_vs.towtracker.order.presentation.tracks.TracksFragment
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import java.math.BigDecimal
import java.util.Locale
import kotlin.getValue


class MainOrderFragment : Fragment(), FragmentNavigationListener {
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
    private val mainViewModel: MainViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }
    private val mapViewModel: MapViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        observeData()
        onCompleteOrderClick()
        observeOrderState()

        binding.ivSavePdf.setOnClickListener {
            val fileName = TimeUtils.getDate()
            orderViewModel.savePdf(true, requireContext(), fileName)
        }
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

    class ViewPagerAdapter(fragment: Fragment, private val list: List<Fragment>) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return list.size
        }
        override fun createFragment(position: Int): Fragment {
            return list[position]
        }
    }

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
                mapViewModel.updateProgress(View.GONE)
                openParentFragment(MapFragment.Companion.newInstance())
            } else {
                orderViewModel.emptyOrder()
                servicesViewModel.deleteAllServices()
                tracksViewModel.deleteAllTracks()
                openParentFragment(NewOrderFragment.Companion.newInstance())
            }
        }
    }

    private fun observeOrderState() {
        orderViewModel.isOrderStarted.observe(viewLifecycleOwner) {
            mainViewModel.changeBottomNavigation(CurrentScreen.ORDER)
        }
    }

    override fun openNewFragment(f: Fragment) {
        openParentFragmentBackstack(f)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainOrderFragment()
    }
}
