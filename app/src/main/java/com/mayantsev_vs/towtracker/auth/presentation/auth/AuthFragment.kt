package com.mayantsev_vs.towtracker.auth.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mayantsev_vs.towtracker.databinding.FragmentAuthBinding
import com.mayantsev_vs.towtracker.main.presentation.MainFragment
import com.mayantsev_vs.towtracker.main.utils.openMainFragment
import com.mayantsev_vs.towtracker.main.utils.showToast
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import kotlin.getValue

class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginViewModel: AuthViewModel by activityViewModels {
            ViewModelFactory(requireContext().applicationContext)
        }

        binding.loginButton.setOnClickListener {
            if (loginViewModel.stateLiveData.value != AuthUiState.Login) {
                if (binding.emailTextView.text.toString().isEmpty()) {
                    showToast("E-mail не может быть пустым!")
                } else if (binding.passwordTextView.text.toString().isEmpty()) {
                    showToast("Пароль не может быть пустым!")
                } else if (binding.repeatPasswordTextView.text.toString() != binding.passwordTextView.text.toString()) {
                    showToast("Пароли не совпадают!")
                } else {
                    loginViewModel.register(
                        binding.emailTextView.text.toString(),
                        binding.usernameTextView.text.toString(),
                        binding.passwordTextView.text.toString()
                    )
                }
            } else {
                if (binding.emailTextView.text.toString().isEmpty()) {
                    showToast("Поле E-mail не может быть пустым!")
                } else if (binding.passwordTextView.text.toString().isEmpty()) {
                    showToast("Поле Пароль не может быть пустым!")
                } else {
                    loginViewModel.login(
                        binding.emailTextView.text.toString(),
                        binding.passwordTextView.text.toString()
                    )
                }
            }
        }

        binding.registered.setOnClickListener {
            if (loginViewModel.stateLiveData.value != AuthUiState.Login) {
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
                binding.usernameTextInputLayout,
                binding.repeatedPasswordTextInputLayout,
                binding.loginButton
            )
        }

        loginViewModel.navigationLiveData.observe(viewLifecycleOwner) {
            if (it) {
                openMainFragment(MainFragment())
            }
        }

        loginViewModel.error.observe(viewLifecycleOwner) { error ->
            if (loginViewModel.error.value?.isNotEmpty() == true) {
                showToast(error)
            }
        }

        loginViewModel.progressLiveData.observe(viewLifecycleOwner) {
            binding.loginProgress.visibility = it
        }

    }
}