package com.mayantsev_vs.towtracker.login.presentation.profile

import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mayantsev_vs.towtracker.R

interface ProfileUiState {

    fun apply(
        usernameTextLayout: TextInputLayout,
        passwordInputLayout: TextInputLayout,
        editButton: Button,
        cancelButton: Button,
        passwordTextView: TextView,
        repeatedPasswordLinear: LinearLayout,
        newPasswordLinear: LinearLayout,
        newPasswordTextLayout: TextInputLayout,
        repeatedTextLayout: TextInputLayout,
        newPasswordEditText: TextInputEditText,
        repeatedPasswordEditText: TextInputEditText
    )

    object Edit: ProfileUiState {
        override fun apply(
            usernameTextLayout: TextInputLayout,
            passwordInputLayout: TextInputLayout,
            editButton: Button,
            cancelButton: Button,
            passwordTextView: TextView,
            repeatedPasswordLinear: LinearLayout,
            newPasswordLinear: LinearLayout,
            newPasswordTextLayout: TextInputLayout,
            repeatedTextLayout: TextInputLayout,
            newPasswordEditText: TextInputEditText,
            repeatedPasswordEditText: TextInputEditText
        ) {
            usernameTextLayout.isEnabled = true
            passwordInputLayout.isEnabled = true
            editButton.text = editButton.context.getString(R.string.save_button)
            cancelButton.visibility = View.VISIBLE
            passwordTextView.text = "Старый пароль"
            repeatedPasswordLinear.visibility = View.VISIBLE
            newPasswordLinear.visibility = View.VISIBLE
            newPasswordTextLayout.isEnabled = true
            repeatedTextLayout.isEnabled = true
        }
    }

    object Read: ProfileUiState {
        override fun apply(
            usernameTextLayout: TextInputLayout,
            passwordInputLayout: TextInputLayout,
            editButton: Button,
            cancelButton: Button,
            passwordTextView: TextView,
            repeatedPasswordLinear: LinearLayout,
            newPasswordLinear: LinearLayout,
            newPasswordTextLayout: TextInputLayout,
            repeatedTextLayout: TextInputLayout,
            newPasswordEditText: TextInputEditText,
            repeatedPasswordEditText: TextInputEditText
        ) {
            usernameTextLayout.isEnabled = false
            passwordInputLayout.isEnabled = false
            editButton.text = editButton.context.getString(R.string.edit)
            cancelButton.visibility = View.GONE
            passwordTextView.text = "Пароль"
            repeatedPasswordLinear.visibility = View.GONE
            newPasswordLinear.visibility = View.GONE
            newPasswordTextLayout.isEnabled = false
            repeatedTextLayout.isEnabled = false
            newPasswordEditText.text?.clear()
            repeatedPasswordEditText.text?.clear()
        }
    }

}