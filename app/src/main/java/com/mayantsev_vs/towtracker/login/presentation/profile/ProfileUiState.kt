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
        repeatedPasswordEditText: TextInputEditText,
        passwordEditText: TextInputEditText,
        passwordLinear: LinearLayout
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
            repeatedPasswordEditText: TextInputEditText,
            passwordEditText: TextInputEditText,
            passwordLinear: LinearLayout
        ) {
            usernameTextLayout.isEnabled = true
            passwordInputLayout.isEnabled = true
            newPasswordTextLayout.isEnabled = true
            repeatedTextLayout.isEnabled = true
            editButton.text = editButton.context.getString(R.string.save_button)
            cancelButton.visibility = View.VISIBLE
            repeatedPasswordLinear.visibility = View.VISIBLE
            newPasswordLinear.visibility = View.VISIBLE
            passwordLinear.visibility = View.VISIBLE

//            newPasswordEditText.text?.clear()
//            repeatedPasswordEditText.text?.clear()
//            passwordEditText.text?.clear()
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
            repeatedPasswordEditText: TextInputEditText,
            passwordEditText: TextInputEditText,
            passwordLinear: LinearLayout
        ) {
            usernameTextLayout.isEnabled = false
            passwordInputLayout.isEnabled = false
            newPasswordTextLayout.isEnabled = false
            repeatedTextLayout.isEnabled = false
            editButton.text = editButton.context.getString(R.string.edit)
            cancelButton.visibility = View.GONE
            repeatedPasswordLinear.visibility = View.GONE
            newPasswordLinear.visibility = View.GONE
            passwordLinear.visibility = View.GONE
        }
    }

}