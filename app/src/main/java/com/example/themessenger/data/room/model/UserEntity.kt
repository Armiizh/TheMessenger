package com.example.themessenger.data.room.model

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    val name: String ?= null,
    val username: String ?= null,
    val birthday: String ?= null,
    val city: String ?= null,
    val vk: String ?= null,
    val instagram: String ?= null,
    val status: String ?= null,
    val avatar: String ?= null,
    @PrimaryKey
    val id: Int ?= null,
    val last: String ?= null,
    val online: Boolean ?= null,
    val created: String ?= null,
    val phone: String ?= null,
    val completedTask: Int ?= null,
    val avatars: Avatars?,
    val zodiacSign: String ?= null
)
data class Avatars(
    val avatar: String ?= null,
    val bigAvatar: String ?= null,
    val miniAvatar: String ?= null
)