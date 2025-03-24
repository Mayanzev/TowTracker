package com.mayantsev_vs.towtracker.login.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mayantsev_vs.towtracker.databinding.FragmentProfileBinding
import com.mayantsev_vs.towtracker.login.presentation.login.LoginFragment
import com.mayantsev_vs.towtracker.login.presentation.login.LoginViewModel
import com.mayantsev_vs.towtracker.main.utils.openFragment
import com.mayantsev_vs.towtracker.main.utils.showToast
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import kotlin.getValue


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val loginViewModel: LoginViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }
    private val profileViewModel: ProfileViewModel by activityViewModels {
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

        loginViewModel.getUser()

        profileViewModel.changeEdit(false)

        binding.exitButton.setOnClickListener {
            loginViewModel.clearUser()
            (requireActivity() as AppCompatActivity).openFragment(LoginFragment())
        }

        binding.editButton.setOnClickListener {
            if (profileViewModel.profileState.value == ProfileUiState.Edit) {
                if (binding.passwordEditText.text.toString().isEmpty()) {
                    profileViewModel.updateUser(
                        binding.emailEditText.text.toString(),
                        binding.usernameEditText.text.toString()
                    )
                    profileViewModel.changeEdit(profileViewModel.profileState.value != ProfileUiState.Edit)
                } else if (binding.usernameEditText.text.toString().isEmpty()) {
                    if (binding.newPasswordEditText.text.toString() == binding.repeatedPasswordEditText.text.toString()) {
                        profileViewModel.updateUserPassword(
                            binding.emailEditText.text.toString(),
                            binding.passwordEditText.text.toString(),
                            binding.newPasswordEditText.text.toString()
                        )
                    } else {
                        showToast("Пароли не совпадают!")
                    }
                }
            } else {
                profileViewModel.changeEdit(profileViewModel.profileState.value != ProfileUiState.Edit)
            }
        }

        profileViewModel.error.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) showToast(it)
        }

        loginViewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.emailEditText.setText(it.login)
            binding.usernameEditText.setText(it.username)
        }

        profileViewModel.profileState.observe(viewLifecycleOwner) { profileState ->
            profileState.apply(
                binding.usernameTextLayout,
                binding.passwordTextLayout,
                binding.editButton,
                binding.cancelButton,
                binding.passwordTextView,
                binding.repeatedPasswordLinear,
                binding.newPasswordLinear,
                binding.newPasswordTextLayout,
                binding.repeatedPasswordTextLayout,
                binding.newPasswordEditText,
                binding.repeatedPasswordEditText,
                binding.passwordEditText,
                binding.passwordLinear
            )
        }

        loginViewModel.error.observe(viewLifecycleOwner) { error ->
            if (loginViewModel.error.value?.isNotEmpty() == true) {
                showToast(error)
                binding.errorTextView.visibility = View.VISIBLE
            }
        }

        binding.cancelButton.setOnClickListener {
            loginViewModel.getUser()
            profileViewModel.changeEdit(false)
        }

        loginViewModel.progressLiveData.observe(viewLifecycleOwner) {
            binding.profileProgress.visibility = it
        }
    }
}