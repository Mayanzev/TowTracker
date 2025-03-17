package com.mayantsev_vs.towtracker.login.presentation

import com.mayantsev_vs.towtracker.R
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

abstract class UiState {

    abstract fun apply(
        emailTextView: TextInputEditText,
        usernameTextView: TextInputEditText,
        passwordTextView: TextInputEditText,
        repeatedPasswordTextView: TextInputEditText,
        registeredTextView: TextView,
        button: Button
    )

    protected fun clear(
        emailTextView: TextInputEditText,
        usernameTextView: TextInputEditText,
        passwordTextView: TextInputEditText,
        repeatedPasswordTextView: TextInputEditText
    ) {
        emailTextView.text?.clear()
        usernameTextView.text?.clear()
        passwordTextView.text?.clear()
        repeatedPasswordTextView.text?.clear()
    }

    object Login : UiState() {
        override fun apply(
            emailTextView: TextInputEditText,
            usernameTextView: TextInputEditText,
            passwordTextView: TextInputEditText,
            repeatedPasswordTextView: TextInputEditText,
            registeredTextView: TextView,
            button: Button
        ) {
            clear(emailTextView, usernameTextView, passwordTextView, repeatedPasswordTextView)
            usernameTextView.visibility = View.GONE
            repeatedPasswordTextView.visibility = View.GONE
            registeredTextView.text = button.context.getString(R.string.register)
            button.text = button.context.getString(R.string.enter)
        }
    }

    object Register : UiState() {
        override fun apply(
            emailTextView: TextInputEditText,
            usernameTextView: TextInputEditText,
            passwordTextView: TextInputEditText,
            repeatedPasswordTextView: TextInputEditText,
            registeredTextView: TextView,
            button: Button
        ) {
            clear(emailTextView, usernameTextView, passwordTextView, repeatedPasswordTextView)
            usernameTextView.visibility = View.VISIBLE
            repeatedPasswordTextView.visibility = View.VISIBLE
            registeredTextView.text = button.context.getString(R.string.registered)
            button.text = button.context.getString(R.string.login)
        }
    }

}