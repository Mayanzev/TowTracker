package com.mayantsev_vs.towtracker.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.fragment.app.activityViewModels
import com.mayantsev_vs.towtracker.MainApp
import com.mayantsev_vs.towtracker.MainViewModel
import com.mayantsev_vs.towtracker.databinding.ViewTrackBinding
import org.osmdroid.config.Configuration
import kotlin.getValue

class ViewTrackFragment : Fragment() {
    private lateinit var binding: ViewTrackBinding
    private val model: MainViewModel by activityViewModels {
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsOsm()
        binding = ViewTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTrack()
    }

    private fun getTrack() = with(binding) {
        model.currentTrack.observe(viewLifecycleOwner) {

            val date = it.date
            val time = it.time
            val speed = "Average speed: ${it.speed} km/h"
            val distance = "Distance: ${it.distance} km"

            tvDate.text = date
            tvTime.text = time
            tvAverageVel.text = speed
            tvDistance.text = distance
        }
    }

    private fun settingsOsm() {
        Configuration.getInstance().load(
            activity as ComponentActivity,
            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        val userAgent = "com.mayantsev_vs.towtracker/1.0"
        Configuration.getInstance().userAgentValue = userAgent
    }

    companion object {
        @JvmStatic
        fun newInstance() = ViewTrackFragment()
    }
}