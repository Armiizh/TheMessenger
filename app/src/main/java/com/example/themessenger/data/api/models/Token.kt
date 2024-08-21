package com.example.themessenger.data.api.models

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("refresh_token")
    val refresh_token: String ?= null,
    @SerializedName("access_token")
    val access_token: String ?= null,
    @SerializedName("user_id")
    val user_id: Int ?= null
)