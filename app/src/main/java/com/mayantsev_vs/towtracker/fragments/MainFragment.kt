package com.mayantsev_vs.towtracker.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import com.mayantsev_vs.towtracker.databinding.FragmentMainBinding
import org.osmdroid.config.Configuration
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsOsm()
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOSM()
    }

    private fun settingsOsm () {
        Configuration.getInstance().load(
            activity as ComponentActivity,
            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        val userAgent = "com.mayantsev_vs.towtracker/1.0"
        Configuration.getInstance().userAgentValue = userAgent
    }

    private fun initOSM() = with(binding) {
        map.controller.setZoom(20.0)
        val mLocProvider = GpsMyLocationProvider(activity)
        val mLocOverlay = MyLocationNewOverlay(mLocProvider, map)
        mLocOverlay.enableMyLocation()
        mLocOverlay.enableFollowLocation()
        mLocOverlay.runOnFirstFix {
            map.overlays.clear()
            map.overlays.add(mLocOverlay)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}