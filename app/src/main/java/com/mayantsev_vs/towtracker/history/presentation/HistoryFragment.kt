package com.mayantsev_vs.towtracker.history.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mayantsev_vs.towtracker.databinding.FragmentHistoryBinding
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import kotlin.getValue

class HistoryFragment: Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val historyViewModel: HistoryViewModel by activityViewModels {
            ViewModelFactory(requireContext().applicationContext)
        }
        historyViewModel.getHistory()
        val adapter = HistoryAdapter()
        binding.historyList.adapter = adapter
        historyViewModel.historyLiveData.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.map {
                HistoryUiItem(
                    it.date,
                    it.price
                )
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}