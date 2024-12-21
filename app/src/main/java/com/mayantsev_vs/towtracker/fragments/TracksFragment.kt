package com.mayantsev_vs.towtracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mayantsev_vs.towtracker.MainApp
import com.mayantsev_vs.towtracker.MainViewModel
import com.mayantsev_vs.towtracker.databinding.TracksBinding
import com.mayantsev_vs.towtracker.db.TrackAdapter
import com.mayantsev_vs.towtracker.db.TrackItem
import kotlin.getValue

class TracksFragment : Fragment(), TrackAdapter.Listener {
    private lateinit var binding: TracksBinding
    private lateinit var adapter: TrackAdapter
    private val model: MainViewModel by activityViewModels {
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        getTracks()
    }

    private fun getTracks() {
        model.tracks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.tvEmpty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun initRcView() = with(binding) {
        adapter = TrackAdapter(this@TracksFragment)
        rcView.layoutManager = LinearLayoutManager(requireContext())
        rcView.adapter = adapter
    }

    override fun onClick(track: TrackItem) {
        model.deleteTrack(track)
    }


    companion object {
        @JvmStatic
        fun newInstance() = TracksFragment()
    }
}