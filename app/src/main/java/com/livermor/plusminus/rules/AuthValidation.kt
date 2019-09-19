package com.livermor.plusminus.rules

import android.content.Context
import com.livermor.plusminus.R
import com.livermor.plusminus.stringRes


fun errorLabel(login: String, password: String, passwordConfirm: String): String =
    when {
        login.length < 4 || login.isBlank() -> R.string.login_req_length
        login.matches(Regex("^[a-zA-Z0-9.\\-_%+1]+")).not() -> R.string.login_req_chars

        password.length < 4 -> R.string.password_req_length
        password.contains(Regex("\\d")).not() -> R.string.password_req_digit
        password.contains(Regex("[a-z]")).not() -> R.string.password_req_lower
        password.contains(Regex("[A-Z]")).not() -> R.string.password_req_upper

        passwordConfirm != password -> R.string.password_req_confirm

        else -> R.string.empty
    }.let { stringRes(it) }

