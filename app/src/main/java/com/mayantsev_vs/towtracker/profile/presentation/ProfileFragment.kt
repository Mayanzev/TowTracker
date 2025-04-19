package com.mayantsev_vs.towtracker.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mayantsev_vs.towtracker.main.utils.openParentFragmentBackstack
import com.mayantsev_vs.towtracker.databinding.FragmentProfileBinding
import com.mayantsev_vs.towtracker.history.presentation.HistoryFragment
import com.mayantsev_vs.towtracker.login.presentation.profile.UserProfileFragment
import com.mayantsev_vs.towtracker.settings.presentation.SettingsFragment

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
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
}
