package com.mayantsev_vs.towtracker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mayantsev_vs.towtracker.presentation.MainApp
import com.mayantsev_vs.towtracker.presentation.MainViewModel
import com.mayantsev_vs.towtracker.data.db.TrackAdapter
import com.mayantsev_vs.towtracker.data.db.TrackItem
import com.mayantsev_vs.towtracker.data.utils.openFragment
import com.mayantsev_vs.towtracker.databinding.FragmentTracksBinding
import kotlin.getValue

class TracksFragment : Fragment(), TrackAdapter.Listener {
    private lateinit var binding: FragmentTracksBinding
    private lateinit var adapter: TrackAdapter
    private val model: MainViewModel by activityViewModels {
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).database)
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
        model.tracks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.tvEmptyTracks.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onClick(track: TrackItem, type: TrackAdapter.ClickType) {
        when (type) {
            TrackAdapter.ClickType.DELETE -> model.deleteTrack(track)
            TrackAdapter.ClickType.OPEN -> {
                model.currentTrack.value = track
                openFragment(ViewTrackFragment.newInstance())
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = TracksFragment()
    }
}