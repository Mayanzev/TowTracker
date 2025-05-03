package com.mayantsev_vs.towtracker.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mayantsev_vs.towtracker.main.utils.openParentFragmentBackstack
import com.mayantsev_vs.towtracker.databinding.FragmentProfileBinding
import com.mayantsev_vs.towtracker.history.presentation.HistoryFragment
import com.mayantsev_vs.towtracker.userProfile.presentation.UserProfileFragment
import com.mayantsev_vs.towtracker.settings.presentation.SettingsFragment

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        onBottomClick()
        return binding.root
    }

    private fun onBottomClick() {
        binding.btnProfile.setOnClickListener {
            openParentFragmentBackstack(UserProfileFragment())
        }
        binding.btnHistory.setOnClickListener {
            openParentFragmentBackstack(HistoryFragment())
        }
        binding.btnSettings.setOnClickListener {
            openParentFragmentBackstack(SettingsFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
