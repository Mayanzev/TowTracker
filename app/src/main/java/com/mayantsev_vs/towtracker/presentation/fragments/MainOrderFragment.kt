package com.mayantsev_vs.towtracker.presentation.fragments

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
import com.mayantsev_vs.towtracker.presentation.MainApp
import com.mayantsev_vs.towtracker.presentation.MainViewModel
import java.math.BigDecimal
import java.util.Locale
import kotlin.getValue


class MainOrderFragment : Fragment() {
    private val fragList = listOf(
        TracksFragment.newInstance(),
        ServicesFragment.newInstance()
    )
    private lateinit var binding: FragmentMainOrderBinding
    private val model: MainViewModel by activityViewModels {
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).database)
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

        val adapter = ViewPagerAdapter(this, fragList)
        binding.viewPager.adapter = adapter

        val tabTitles = resources.getStringArray(R.array.fragments_list_titles)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) {
            tab, pos -> tab.text = tabTitles[pos]
        }.attach()

        model.services.observe(viewLifecycleOwner) { serviceList ->
            updateTotalPrice(serviceList, model.tracks.value ?: emptyList())
        }

        model.tracks.observe(viewLifecycleOwner) { trackList ->
            updateTotalPrice(model.services.value ?: emptyList(), trackList)
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

    // calculates the total price by summing up the prices of services and tracks, then updates the UI
    private fun updateTotalPrice(serviceList: List<ServiceItem>, trackList: List<TrackItem>) {
        val totalServicePrice = serviceList.sumOf { it.price.toBigDecimalOrNull() ?: BigDecimal.ZERO }
        val totalTrackPrice = trackList.sumOf { it.price.toBigDecimalOrNull() ?: BigDecimal.ZERO }
        val totalPrice = totalServicePrice + totalTrackPrice
        val formattedPrice = String.format(Locale.US, "%.2f", totalPrice)
        binding.tvTotalPrice.text = getString(R.string.total_price_label, formattedPrice)
    }


    companion object {
        @JvmStatic
        fun newInstance() = MainOrderFragment()
    }
}