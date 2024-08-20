package com.example.themessenger.data.api.models

import com.google.gson.annotations.SerializedName

data class Avatars(
    @SerializedName("avatar")
    val avatar:	String ?= null,
    @SerializedName("bigAvatar")
    val bigAvatar:	String ?= null,
    @SerializedName("miniAvatar")
    val miniAvatar: String ?= null
)
