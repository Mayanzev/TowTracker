package com.mayantsev_vs.towtracker.login.presentation

import android.widget.Button
import com.google.android.material.textfield.TextInputLayout
import com.mayantsev_vs.towtracker.R

interface ProfileUiState {

    fun apply(
        usernameTextLayout: TextInputLayout,
        passwordInputLayout: TextInputLayout,
        editButton: Button
    )

    object Edit: ProfileUiState {
        override fun apply(
            usernameTextLayout: TextInputLayout,
            passwordInputLayout: TextInputLayout,
            editButton: Button
        ) {
            usernameTextLayout.isEnabled = true
            passwordInputLayout.isEnabled = true
            editButton.text = editButton.context.getString(R.string.save_button)
        }
    }

    object Read: ProfileUiState {
        override fun apply(
            usernameTextLayout: TextInputLayout,
            passwordInputLayout: TextInputLayout,
            editButton: Button
        ) {
            usernameTextLayout.isEnabled = false
            passwordInputLayout.isEnabled = false
            editButton.text = editButton.context.getString(R.string.edit)
        }
    }

}