package com.example.themessenger.data.api.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("profile_data")
    val profileData: ProfileData
)

data class ProfileData(
    @SerializedName("name")
    val name: String ?= null,
    @SerializedName("username")
    val username: String ?= null,
    @SerializedName("birthday")
    val birthday: String ?= null,
    @SerializedName("city")
    val city: String ?= null,
    @SerializedName("vk")
    val vk: String ?= null,
    @SerializedName("instagram")
    val instagram: String ?= null,
    @SerializedName("status")
    val status: String ?= null,
    @SerializedName("avatar")
    val avatar: String ?= null,
    @SerializedName("id")
    val id: Int ?= null,
    @SerializedName("last")
    val last: String ?= null,
    @SerializedName("online")
    val online: Boolean ?= null,
    @SerializedName("created")
    val created: String ?= null,
    @SerializedName("phone")
    val phone: String ?= null,
    @SerializedName("completed_task")
    val completedTask: Int ?= null,
    @SerializedName("avatars")
    val avatars: Avatars
)

data class Avatars(
    @SerializedName("avatar")
    val avatar: String ?= null,
    @SerializedName("bigAvatar")
    val bigAvatar: String ?= null,
    @SerializedName("miniAvatar")
    val miniAvatar: String ?= null
)