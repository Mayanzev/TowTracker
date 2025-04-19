package com.mayantsev_vs.towtracker.main.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.main.utils.openFragment
import com.mayantsev_vs.towtracker.databinding.FragmentMainBinding
import com.mayantsev_vs.towtracker.map.presentation.MapFragment
import com.mayantsev_vs.towtracker.order.presentation.MainOrderFragment
import com.mayantsev_vs.towtracker.order.presentation.NewOrderFragment
import com.mayantsev_vs.towtracker.order.presentation.OrderViewModel
import com.mayantsev_vs.towtracker.profile.presentation.ProfileFragment
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import kotlin.getValue

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val orderViewModel: OrderViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }
    private val mainViewModel: MainViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomNavigation.selectedItemId = R.id.id_order

        if (savedInstanceState == null) {
            openFragment(MainOrderFragment())
        }

        onBottomNavClick()

        orderViewModel.isOrderStarted.observe(viewLifecycleOwner, Observer { isOrderStarted ->
            if (isOrderStarted) {
                openFragment(MainOrderFragment.newInstance())
            } else {
                openFragment(NewOrderFragment.newInstance())
            }
        })

        mainViewModel.bottomNavigation.observe(viewLifecycleOwner) {
            binding.bottomNavigation.selectedItemId = when (it) {
                CurrentScreen.MAP -> R.id.id_map
                CurrentScreen.ORDER -> R.id.id_order
                CurrentScreen.SETTINGS -> R.id.id_main_settings
            }
        }

    }

    private fun onBottomNavClick() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_order -> {
                    if (mainViewModel.bottomNavigation.value != CurrentScreen.ORDER) {
                        orderViewModel.isOrderStarted.value?.let { isOrderStarted ->
                            if (isOrderStarted) {
                                openFragment(MainOrderFragment.newInstance())
                            } else {
                                openFragment(NewOrderFragment.newInstance())
                            }
                        }
                        mainViewModel.changeBottomNavigation(CurrentScreen.ORDER)
                    }
                }

                R.id.id_map -> {
                    if (mainViewModel.bottomNavigation.value != CurrentScreen.MAP) {
                        openFragment(MapFragment.newInstance())
                        mainViewModel.changeBottomNavigation(CurrentScreen.MAP)
                    }
                }

                R.id.id_main_settings -> {
                    if (mainViewModel.bottomNavigation.value != CurrentScreen.SETTINGS) {
                        openFragment(ProfileFragment())
                        mainViewModel.changeBottomNavigation(CurrentScreen.SETTINGS)
                    }
                }
            }
            true
        }
    }


    enum class CurrentScreen {
        MAP, ORDER, SETTINGS
    }
}