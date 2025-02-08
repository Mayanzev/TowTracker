package com.mayantsev_vs.towtracker.presentation.fragments

import com.mayantsev_vs.towtracker.data.utils.PreferencesHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mayantsev_vs.towtracker.databinding.FragmentNewOrderBinding
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.data.utils.ViewModelFactory
import com.mayantsev_vs.towtracker.presentation.MainApp
import com.mayantsev_vs.towtracker.presentation.MainViewModel
import kotlin.getValue


class NewOrderFragment : Fragment() {

    private lateinit var binding: FragmentNewOrderBinding
    private val model: MainViewModel by activityViewModels {
        ViewModelFactory((requireContext().applicationContext as MainApp).database, PreferencesHelper(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewOrderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onNewOrderClick()
    }

    private fun onNewOrderClick() {
        binding.btnNewOrder.setOnClickListener {
            model.startNewOrder()
            val mainOrderFragment = MainOrderFragment.newInstance()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragments_container, mainOrderFragment)
                ?.commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = NewOrderFragment()
    }
}
