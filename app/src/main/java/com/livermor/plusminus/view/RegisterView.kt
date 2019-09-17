package com.livermor.plusminus.view

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.livermor.plusminus.R
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class RegisterView(
    context: Context,
    private val onLogin: (login: String, password: String, me: RegisterView) -> Unit
) : RenderableView(context) {

    private var login = ""
    private var password = ""
    private var passwordConfirm = ""
    private var loginFailed = false
    private var isLoggingIn = false

    private val errorLabel: String
        get() {
            val res = when {
                login.length < 4 || login.isBlank() -> R.string.login_req_length
                login.matches(Regex("^[a-zA-Z0-9.\\-_%+1]+")).not() -> R.string.login_req_chars

                password.length < 4 -> R.string.password_req_length
                password.contains(Regex("\\d")).not() -> R.string.password_req_digit
                password.contains(Regex("[a-z]")).not() -> R.string.password_req_lower
                password.contains(Regex("[A-Z]")).not() -> R.string.password_req_upper

                passwordConfirm != password -> R.string.password_req_confirm

                else -> R.string.empty
            }
            return context.getString(res)
        }

    private val isLoginAllowed: Boolean get() = errorLabel.isEmpty()

    private fun onRegisterClicked(v: View) {
        isLoggingIn = true
        println("performing login with login: $login and password $password")
        onLogin(login, password, this)
    }

    fun onRegister(success: Boolean) {
        isLoggingIn = false
        loginFailed = success.not()
        if (success) {
            Toast.makeText(context, R.string.register_success, Toast.LENGTH_SHORT).show()
        }
        Anvil.render()
    }

    override fun view() {
        linearLayout {
            size(FILL, WRAP)
            padding(dip(16))
            orientation(LinearLayout.VERTICAL)
            progressBar { visibility(isLoggingIn) }

            //error
            textView {
                textColor(Color.RED)
                if (errorLabel.isNotEmpty()) {
                    text(errorLabel)
                } else {
                    text(R.string.login_error_label)
                }
                visibility(loginFailed or errorLabel.isNotEmpty())
            }
            //login input
            editText {
                size(FILL, WRAP)
                hint(R.string.login_hint)
                enabled(isLoggingIn.not())
                text(login)
                onTextChanged { s -> login = s.toString() }
            }
            //password input
            editText {
                size(FILL, WRAP)
                hint(R.string.password_hint)
                inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                enabled(isLoggingIn.not())
                text(password)
                onTextChanged { s -> password = s.toString() }
            }
            //password confirm
            editText {
                size(FILL, WRAP)
                hint(R.string.password_confirm)
                inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                enabled(isLoggingIn.not())
                text(passwordConfirm)
                onTextChanged { s -> passwordConfirm = s.toString() }
            }
            button {
                size(FILL, WRAP)
                text(R.string.register_button)
                enabled(isLoginAllowed && !isLoggingIn)
                onClick(this::onRegisterClicked)
            }
        }
    }
}
