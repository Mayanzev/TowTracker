package com.mayantsev_vs.towtracker.login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mayantsev_vs.towtracker.data.utils.openMainFragment
import com.mayantsev_vs.towtracker.databinding.LoginScreenBinding
import com.mayantsev_vs.towtracker.main.presentation.MainFragment
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import kotlin.getValue

class LoginFragment : Fragment() {

    private lateinit var binding: LoginScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginViewModel: LoginViewModel by activityViewModels {
            ViewModelFactory(requireContext().applicationContext)
        }

        binding.loginButton.setOnClickListener {
            if (loginViewModel.stateLiveData.value != UiState.Login) {
                loginViewModel.register(
                    binding.emailTextView.text.toString(),
                    binding.usernameTextView.text.toString(),
                    binding.passwordTextView.text.toString(),
                    binding.repeatPasswordTextView.text.toString()
                )
            } else {
                loginViewModel.login(
                    binding.emailTextView.text.toString(),
                    binding.passwordTextView.text.toString()
                )
            }
        }

        binding.registered.setOnClickListener {
            if (loginViewModel.stateLiveData.value != UiState.Login) {
                loginViewModel.updateRegistered(true)
            } else {
                loginViewModel.updateRegistered(false)
            }
        }

        loginViewModel.stateLiveData.observe(viewLifecycleOwner) {
            it.apply(
                binding.emailTextView,
                binding.usernameTextView,
                binding.passwordTextView,
                binding.repeatPasswordTextView,
                binding.registered,
                binding.loginButton
            )
        }

        loginViewModel.navigationLiveData.observe(viewLifecycleOwner) {
            if (it) {
                openMainFragment(MainFragment())
            }
        }
    }

}