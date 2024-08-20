package com.example.themessenger.data.api.models

import com.google.gson.annotations.SerializedName

data class CheckAuthCode(
    @SerializedName("phone")
    val phone: String ?= null,
    @SerializedName("code")
    val code: String ?= null
)
