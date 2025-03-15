package com.mayantsev_vs.towtracker.settings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mayantsev_vs.towtracker.databinding.FragmentProfileBinding
import com.mayantsev_vs.towtracker.login.presentation.LoginFragment
import com.mayantsev_vs.towtracker.login.presentation.LoginViewModel
import com.mayantsev_vs.towtracker.main.presentation.MainViewModel
import com.mayantsev_vs.towtracker.main.utils.openFragment
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import kotlin.getValue


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val loginViewModel: LoginViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                }
            }
        )

        binding.exitButton.setOnClickListener {
            loginViewModel.clearUser()
            (requireActivity() as AppCompatActivity).openFragment(LoginFragment())
        }
    }
}