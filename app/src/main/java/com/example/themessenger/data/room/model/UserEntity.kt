package com.example.themessenger.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int?,
    val avatar: String?,
    val phone: String?,
    val nickname: String?,
    val city: String?,
    val birthday: String?,
    val zodiac: String?,
    val about: String?
)