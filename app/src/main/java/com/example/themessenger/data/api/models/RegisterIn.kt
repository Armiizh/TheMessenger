package com.example.themessenger.data.api.models

import com.google.gson.annotations.SerializedName

data class RegisterIn(
    @SerializedName("phone")
    val phone:String ?= null,
    @SerializedName("name")
    val name:String ?= null,
    @SerializedName("username")
    val username:String ?= null
)
