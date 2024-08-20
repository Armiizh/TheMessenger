package com.example.themessenger.data.api.models

import com.google.gson.annotations.SerializedName

data class IsSuccess(
    @SerializedName("is_success")
    val isSuccess: Boolean ?= null
)
