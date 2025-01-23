package com.mayantsev_vs.towtracker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mayantsev_vs.towtracker.databinding.FragmentMainOrderBinding
import com.mayantsev_vs.towtracker.R


class MainOrderFragment : Fragment() {
    private val fragList = listOf(
        TracksFragment.newInstance(),
        ServicesFragment.newInstance()
    )
    private lateinit var binding: FragmentMainOrderBinding

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


    companion object {
        @JvmStatic
        fun newInstance() = MainOrderFragment()
    }
}