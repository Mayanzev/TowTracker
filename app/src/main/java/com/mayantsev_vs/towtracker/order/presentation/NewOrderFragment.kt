package com.mayantsev_vs.towtracker.order.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mayantsev_vs.towtracker.databinding.FragmentAuthBinding
import com.mayantsev_vs.towtracker.databinding.FragmentNewOrderBinding
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import com.mayantsev_vs.towtracker.main.utils.openParentFragment
import kotlin.getValue


class NewOrderFragment : Fragment() {

    private var _binding: FragmentNewOrderBinding? = null
    private val binding get() = _binding!!
    private val orderViewModel: OrderViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onNewOrderClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNewOrderClick() {
        binding.btnNewOrder.setOnClickListener {
            orderViewModel.activeOrder()
            openParentFragment(MainOrderFragment.Companion.newInstance())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = NewOrderFragment()
    }
}
