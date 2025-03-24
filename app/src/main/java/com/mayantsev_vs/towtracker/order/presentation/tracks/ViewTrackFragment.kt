package com.mayantsev_vs.towtracker.order.presentation.tracks

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.mayantsev_vs.towtracker.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import com.mayantsev_vs.towtracker.databinding.FragmentViewTrackBinding
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import kotlin.getValue

class ViewTrackFragment : Fragment() {
    private var startPoint: GeoPoint? = null
    private lateinit var binding: FragmentViewTrackBinding
    private val tracksViewModel: TrackViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsOsm()
        binding = FragmentViewTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTrack()
        binding.ivCenter.setOnClickListener {
            if (startPoint != null) binding.map.controller.animateTo(startPoint)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                }
            }
        )

        binding.map.setMultiTouchControls(true)
        binding.map.zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER)

    }

    // configures OSM settings by loading preferences and setting the user agent
    private fun settingsOsm() {
        Configuration.getInstance().load(
            activity as ComponentActivity,
            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        val userAgent = "com.mayantsev_vs.towtracker/1.0"
        Configuration.getInstance().userAgentValue = userAgent
    }

    // observes the current track and updates UI elements with time, speed, distance, and price
    private fun getTrack() = with(binding) {
        tracksViewModel.currentTrack.observe(viewLifecycleOwner) {

            val time = it.time
            val averageSpeed = "${getString(R.string.average_speed)} ${it.speed} ${getString(R.string.km_h)}"
            val distance = "${getString(R.string.distance)} ${it.distance} ${getString(R.string.km)}"
            val price = "${getString(R.string.price)} ${it.price} ${getString(R.string.currency_symbol)}"

            tvTime.text = time
            tvAverageSpeed.text = averageSpeed
            tvDistance.text = distance
            tvPrice.text = price

            val polyline = getPolyline(it.geoPoints)
            map.overlays.add(polyline)
            setMarkers(polyline.actualPoints)
            goToStartPosition(polyline.actualPoints[0])
            startPoint = polyline.actualPoints[0]
        }
    }

    // creates a Polyline from a string of geo-coordinates, setting its color and adding points
    private fun getPolyline(geoPoints: String): Polyline {
        val polyline = Polyline()
        polyline.outlinePaint.color = Color.parseColor(
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .getString(KEY_COLOR, "#0077FF")
        )
        val list = geoPoints.split("/")
        list.forEach {
            if (it.isEmpty()) return@forEach
            val points = it.split(",")
            polyline.addPoint(GeoPoint(points[0].toDouble(), points[1].toDouble()))
        }
        return polyline
    }

    // moves the map to the start position with an animation and sets the zoom level to 15
    private fun goToStartPosition(startPosition: GeoPoint) {
        binding.map.controller.zoomTo(15.0)
        binding.map.controller.animateTo(startPosition)
    }

    // adds start and finish markers to the map based on the provided list of GeoPoints
    private fun setMarkers(list: List<GeoPoint>) = with(binding) {
        val startMarker = Marker(map)
        val finishMarker = Marker(map)
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        finishMarker.setAnchor(0.2f, Marker.ANCHOR_BOTTOM)
        startMarker.icon = getDrawable(requireContext(), R.drawable.ic_start_position)
        finishMarker.icon = getDrawable(requireContext(), R.drawable.ic_finish_position)
        startMarker.position = list[0]
        finishMarker.position = list[list.size - 1]
        map.overlays.add(startMarker)
        map.overlays.add(finishMarker)
    }

    companion object {
        const val KEY_COLOR = "color_key"
    }
}