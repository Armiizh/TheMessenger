package com.example.themessenger.data.api.models

import com.google.gson.annotations.SerializedName

data class PutUser(
    @SerializedName("avatar")
    val avatar: Avatar,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("instagram")
    val instagram: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("vk")
    val vk: String
)
data class Avatar(
    @SerializedName("filename")
    val filename: String,
    @SerializedName("base_64")
    val base_64: String

)