package com.example.themessenger.data.api.models

import com.google.gson.annotations.SerializedName

data class RefreshToken(
    @SerializedName("refresh_token")
    val refresh_token: String ?= null
)
