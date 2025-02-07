package com.mayantsev_vs.towtracker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mayantsev_vs.towtracker.databinding.FragmentNewOrderBinding
import com.mayantsev_vs.towtracker.R


class NewOrderFragment : Fragment() {

    private lateinit var binding: FragmentNewOrderBinding

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
