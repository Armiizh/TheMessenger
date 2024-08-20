package com.example.themessenger.data.api.models

import com.google.gson.annotations.SerializedName

data class phoneBase (
    @SerializedName("phone")
    val phone: String ?= null
)