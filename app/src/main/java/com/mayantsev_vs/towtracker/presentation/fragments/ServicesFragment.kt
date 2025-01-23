package com.mayantsev_vs.towtracker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mayantsev_vs.towtracker.databinding.FragmentServicesBinding


class ServicesFragment : Fragment() {
    private lateinit var binding: FragmentServicesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }




    companion object {
        @JvmStatic
        fun newInstance() = ServicesFragment()
    }
}