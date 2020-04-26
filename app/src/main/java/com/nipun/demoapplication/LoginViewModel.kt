package com.nipun.demoapplication

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/*
* Created by Nipun Kumar Rajput on 26-04-2020.
* Copyright (c) 2020 Nipun. All rights reserved.
*/
class LoginViewModel : ViewModel() {

    //To validate form states
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    //Function for validation
    fun loginDataChanged(userEmail: String, password: String, confirmPassword: String) {
        if (!userEmail.isValidEmail()) {
            _loginForm.value =
                LoginFormState(userEmailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value =
                LoginFormState(passwordError = R.string.invalid_password)
        } else if (!isPasswordValid(confirmPassword)) {
            _loginForm.value =
                LoginFormState(confirmPassError = R.string.invalid_password)
        } else if (password != confirmPassword) {
            _loginForm.value =
                LoginFormState(confirmPassError = R.string.password_not_match)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this!!).matches()

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

}