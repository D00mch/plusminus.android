package com.livermor.plusminus.model

import com.google.gson.annotations.SerializedName

data class RequestRegister(
    val id: String,
    val pass: String,
    @SerializedName("pass-confirm") val passConfirm: String
)