package com.mayantsev_vs.towtracker.order.presentation.tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mayantsev_vs.towtracker.order.data.cache.TrackItem
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import com.mayantsev_vs.towtracker.databinding.FragmentTracksBinding
import com.mayantsev_vs.towtracker.order.presentation.order.FragmentNavigationListener
import kotlin.getValue

class TracksFragment : Fragment(), TrackAdapter.Listener {
    private lateinit var binding: FragmentTracksBinding
    private lateinit var adapter: TrackAdapter
    private val tracksViewModel: TrackViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        getTracks()
    }

    // initializes RecyclerView with a LinearLayoutManager and sets the adapter
    private fun initRecyclerView() = with(binding) {
        adapter = TrackAdapter(this@TracksFragment)
        rcView.layoutManager = LinearLayoutManager(requireContext())
        rcView.adapter = adapter
    }

    // observes track data and updates the adapter and empty state visibility
    private fun getTracks() {
        tracksViewModel.tracks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.tvEmptyTracks.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onClick(track: TrackItem, type: TrackAdapter.ClickType) {
        when (type) {
            TrackAdapter.ClickType.DELETE -> tracksViewModel.deleteTrack(track)
            TrackAdapter.ClickType.OPEN -> {
                tracksViewModel.currentTrack.value = track
                (parentFragment as FragmentNavigationListener).openNewFragment(ViewTrackFragment())
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = TracksFragment()
    }
}