package com.mayantsev_vs.towtracker.userProfile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mayantsev_vs.towtracker.databinding.FragmentUserProfileBinding
import com.mayantsev_vs.towtracker.auth.presentation.auth.AuthFragment
import com.mayantsev_vs.towtracker.auth.presentation.auth.AuthViewModel
import com.mayantsev_vs.towtracker.main.utils.openFragment
import com.mayantsev_vs.towtracker.main.utils.showToast
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import kotlin.getValue


class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private val userProfileViewModel: UserProfileViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }
    private val loginViewModel: AuthViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
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

        userProfileViewModel.getUser()

        userProfileViewModel.changeEdit(false)

        binding.exitButton.setOnClickListener {
            userProfileViewModel.clearUser()
            loginViewModel.updateNavigation(false)
            (requireActivity() as AppCompatActivity).openFragment(AuthFragment())
        }

        binding.editButton.setOnClickListener {
            if (userProfileViewModel.profileState.value == UserProfileUiState.Edit) {
                if (binding.passwordEditText.text.toString().isEmpty()) {
                    userProfileViewModel.updateUser(
                        binding.emailEditText.text.toString(),
                        binding.usernameEditText.text.toString()
                    )
                    userProfileViewModel.changeEdit(userProfileViewModel.profileState.value != UserProfileUiState.Edit)
                } else {
                    if (binding.newPasswordEditText.text.toString() == binding.repeatedPasswordEditText.text.toString()) {
                        if (binding.newPasswordEditText.text.toString() != binding.passwordEditText.text.toString()) {
                            userProfileViewModel.updateUserPassword(
                                binding.emailEditText.text.toString(),
                                binding.passwordEditText.text.toString(),
                                binding.newPasswordEditText.text.toString()
                            )
                            userProfileViewModel.updateUser(
                                binding.emailEditText.text.toString(),
                                binding.usernameEditText.text.toString()
                            )
                        } else {
                            showToast("Новый пароль такой же как старый!")
                        }
                    } else {
                        showToast("Пароли не совпадают!")
                    }
                }
            } else {
                userProfileViewModel.changeEdit(userProfileViewModel.profileState.value != UserProfileUiState.Edit)
            }
        }

        userProfileViewModel.error.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) showToast(it)
        }

        userProfileViewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.emailEditText.setText(it.login)
            binding.usernameEditText.setText(it.username)
        }

        userProfileViewModel.profileState.observe(viewLifecycleOwner) { profileState ->
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

        userProfileViewModel.networkError.observe(viewLifecycleOwner) { error ->
            if (userProfileViewModel.networkError.value?.isNotEmpty() == true) {
                showToast(error)
                binding.errorTextView.visibility = View.VISIBLE
            }
        }

        binding.cancelButton.setOnClickListener {
            userProfileViewModel.getUser()
            userProfileViewModel.changeEdit(false)
        }

        userProfileViewModel.progressLiveData.observe(viewLifecycleOwner) {
            binding.profileProgress.visibility = it
        }
    }
}