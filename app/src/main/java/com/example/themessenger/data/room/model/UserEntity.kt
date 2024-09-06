package com.example.themessenger.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.themessenger.data.room.Avatar64Converter
import com.example.themessenger.data.room.AvatarsConverter

@Entity(tableName = "users")
@TypeConverters (Avatar64Converter::class)
data class UserEntity(
    var name: String ?= null,
    val username: String ?= null,
    var birthday: String ?= null,
    var city: String ?= null,
    var vk: String ?= null,
    var instagram: String ?= null,
    var status: String ?= null,
    var avatar: String ?= null,
    @PrimaryKey
    val id: Int ?= null,
    val last: String ?= null,
    val online: Boolean ?= null,
    val created: String ?= null,
    val phone: String ?= null,
    val completedTask: Int ?= null,
    val avatars: Avatars?,
    var zodiacSign: String ?= null,
    var avatar64: Avatar64?
)
data class Avatars(
    val avatar: String ?= null,
    val bigAvatar: String ?= null,
    val miniAvatar: String ?= null
)
data class Avatar64(
    val base_64: String ?= null,
    val filename: String ?= null
)
