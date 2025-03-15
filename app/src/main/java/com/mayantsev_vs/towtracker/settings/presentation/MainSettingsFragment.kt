package com.mayantsev_vs.towtracker.settings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mayantsev_vs.towtracker.main.utils.openParentFragmentBackstack
import com.mayantsev_vs.towtracker.databinding.FragmentMainSettingsBinding

class MainSettingsFragment : Fragment() {
    private lateinit var binding: FragmentMainSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainSettingsBinding.inflate(inflater, container, false)
        onBottomClick()
        return binding.root
    }

    private fun onBottomClick() {
        binding.btnProfile.setOnClickListener {
            openParentFragmentBackstack(ProfileFragment())
        }
        binding.btnSettings.setOnClickListener {
            openParentFragmentBackstack(AppearanceSettingsFragment())
        }
    }
}
