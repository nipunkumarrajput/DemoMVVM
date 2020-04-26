package com.nipun.demoapplication

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nipun.demoapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isTablet()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        loginViewModel = ViewModelProvider(this)
            .get(LoginViewModel::class.java)
        observeForErrors()
        viewBinding.userEmail.afterTextChanged {
            loginViewModel.loginDataChanged(
                viewBinding.userEmail.text.toString(),
                viewBinding.password.text.toString(),
                viewBinding.etCPassword.text.toString()
            )
        }

        viewBinding.password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    viewBinding.userEmail.text.toString(),
                    viewBinding.password.text.toString(),
                    viewBinding.etCPassword.text.toString()
                )
            }
        }

        viewBinding.etCPassword.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    viewBinding.userEmail.text.toString(),
                    viewBinding.password.text.toString(),
                    viewBinding.etCPassword.text.toString()
                )
            }
        }

        viewBinding.btnSubmit.setOnClickListener {
            showDialog()
        }
    }

    private fun observeForErrors() {
        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            changeSubmitBtnState(loginState.isDataValid)

            if (loginState.isDataValid) {
                viewBinding.inputEmail.error = null
                viewBinding.inputPassword.error = null
                viewBinding.confirmPassword.error = null
            } else {
                if (loginState.userEmailError != null) {
                    viewBinding.inputEmail.error = getString(loginState.userEmailError)
                } else {
                    viewBinding.inputEmail.error = null
                }
                if (loginState.passwordError != null) {
                    viewBinding.inputPassword.error = getString(loginState.passwordError)
                } else {
                    viewBinding.inputPassword.error = null
                }

                if (loginState.confirmPassError != null) {
                    viewBinding.confirmPassword.error = getString(loginState.confirmPassError)
                } else {
                    viewBinding.confirmPassword.error = null
                }
            }
        })
    }


    private fun changeSubmitBtnState(status: Boolean) {
        viewBinding.btnSubmit.isEnabled = status
        if (status) {
            viewBinding.btnSubmit.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
        } else {
            viewBinding.btnSubmit.setTextColor(ContextCompat.getColor(this, R.color.disableBtnTextColor))
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(this).setTitle("Welcome").setMessage(viewBinding.userEmail.text.toString())
            .setPositiveButton(
                "Ok", null
            ).show()

    }

    fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
