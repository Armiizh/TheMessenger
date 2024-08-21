package com.example.themessenger.data.api.models

import com.google.gson.annotations.SerializedName

data class LoginOut(
    @SerializedName("refresh_token")
    val refresh_token: String ?= null,
    @SerializedName("access_token")
    val access_token: String ?= null,
    @SerializedName("user_id")
    val user_id: Int ?= null,
    @SerializedName("is_user_exists")
    val is_user_exists: Boolean ?= null,
)
