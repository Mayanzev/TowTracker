package com.mayantsev_vs.towtracker.track.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mayantsev_vs.towtracker.track.data.cache.TrackDBO
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import com.mayantsev_vs.towtracker.databinding.FragmentTrackBinding
import com.mayantsev_vs.towtracker.order.presentation.FragmentNavigationListener
import kotlin.getValue

class TrackFragment : Fragment(), TrackAdapter.ClickListener {
    private var _binding: FragmentTrackBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TrackAdapter
    private val tracksViewModel: TrackViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        getTracks()

        binding.swipeLayout.setOnRefreshListener {
            tracksViewModel.updateErrorTracks()
        }

        tracksViewModel.swipeLiveData.observe(viewLifecycleOwner) {
            binding.swipeLayout.isRefreshing = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() = with(binding) {
        adapter = TrackAdapter(this@TrackFragment)
        rcView.layoutManager = LinearLayoutManager(requireContext())
        rcView.adapter = adapter
    }

    private fun getTracks() {
        tracksViewModel.tracks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.tvEmptyTracks.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onClick(track: TrackDBO, type: TrackAdapter.ClickType) {
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
        fun newInstance() = TrackFragment()
    }
}